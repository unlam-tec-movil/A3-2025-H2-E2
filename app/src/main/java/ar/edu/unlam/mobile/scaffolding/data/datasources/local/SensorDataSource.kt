package ar.edu.unlam.mobile.scaffolding.data.datasources.local

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Clase que obtiene la orientación del dispositivo usando sensores.
 *
 * Utiliza TYPE_ROTATION_VECTOR, un sensor de fusión que combina:
 * - Acelerómetro (referencia de gravedad)
 * - Magnetómetro (referencia del norte magnético)
 * - Giroscopio (cambios de rotación precisos)
 *
 * Android aplica algoritmos de fusión para obtener
 * una orientación más precisa y estable que usando sensores individuales por separado.
 *
 * Además, se aplica un filtro para suavizar las lecturas
 * y evitar movimientos "nerviosos/tambaleantes" de la flecha en la UI.
 */
class SensorDataSource
    @Inject
    constructor(
        private val sensorManager: SensorManager,
    ) {
        // Matriz de rotación (3x3 = 9 elementos)
        private val rotationMatrix = FloatArray(9)

        // Ángulos de orientación [azimuth, pitch, roll]
        private val orientationAngles = FloatArray(3)

        // Valor filtrado del azimut (para suavizado)
        private var filteredAzimuth = 0f

        // Bandera para saber si ya tenemos un valor inicial
        private var hasInitialValue = false

        // Constante del filtro low-pass para suavizar lecturas (0.0 a 1.0)
        // Valores más bajos = más suave pero más lento
        // Valores más altos = más rápido pero más nervioso
        companion object {
            private const val LOW_PASS_ALPHA = 0.15f
        }

        /**
         * Función que obtiene un Flow continuo del azimut en grados.
         *
         * Usa TYPE_ROTATION_VECTOR para mayor precisión y estabilidad.
         * Aplica filtro low-pass para suavizar las lecturas.
         *
         * @return Flow que emite el azimut filtrado (0-360 grados) cada vez que cambia.
         */
        fun getOrientation(): Flow<Float> =
            callbackFlow {
                val listener =
                    object : SensorEventListener {
                        override fun onSensorChanged(event: SensorEvent) {
                            if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                                // Convertir el vector de rotación a matriz de rotación
                                SensorManager.getRotationMatrixFromVector(
                                    rotationMatrix,
                                    event.values,
                                )

                                // Obtener los ángulos de orientación desde la matriz
                                SensorManager.getOrientation(rotationMatrix, orientationAngles)

                                // orientationAngles[0] es el azimut en radianes
                                val azimuthRadians = orientationAngles[0]
                                val azimuthDegrees =
                                    Math.toDegrees(azimuthRadians.toDouble()).toFloat()

                                // Normalizar a 0-360
                                val normalizedAzimuth =
                                    if (azimuthDegrees < 0) {
                                        azimuthDegrees + 360
                                    } else {
                                        azimuthDegrees
                                    }

                                // Aplicar filtro low-pass para suavizar
                                val smoothedAzimuth =
                                    if (!hasInitialValue) {
                                        // Primera lectura: usar valor directo
                                        hasInitialValue = true
                                        normalizedAzimuth
                                    } else {
                                        // Lecturas siguientes: aplicar filtro low-pass
                                        // Manejar el cruce de 0°/360° correctamente
                                        lowPassFilter(filteredAzimuth, normalizedAzimuth)
                                    }

                                filteredAzimuth = smoothedAzimuth

                                // Emitir el valor suavizado
                                trySend(smoothedAzimuth)
                            }
                        }

                        override fun onAccuracyChanged(
                            sensor: Sensor?,
                            accuracy: Int,
                        ) {
                            // Se podría notificar al usuario si la precisión es baja, ver luego
                        }
                    }

                // Obtener el sensor de rotación vectorial
                val rotationVectorSensor =
                    sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

                if (rotationVectorSensor != null) {
                    // Registrar listener con tasa de actualización para UI
                    sensorManager.registerListener(
                        listener,
                        rotationVectorSensor,
                        SensorManager.SENSOR_DELAY_UI,
                    )
                } else {
                    // Fallback: Si no hay ROTATION_VECTOR, cerrar el Flow con error
                    close(
                        IllegalStateException(
                            "TYPE_ROTATION_VECTOR no disponible en este dispositivo",
                        ),
                    )
                }

                // Cuando el Flow se cancela, desregistrar el listener
                awaitClose {
                    sensorManager.unregisterListener(listener)
                    // Resetear para la próxima suscripción
                    hasInitialValue = false
                    filteredAzimuth = 0f
                }
            }

        /**
         * Aplica un filtro low-pass considerando el cruce de 0°/360°.
         *
         * Sin este manejo especial, al pasar de 359° a 1°, el filtro
         * interpolaría pasando por 180°, causando un giro incorrecto.
         *
         * @param previous Valor anterior filtrado (0-360)
         * @param current Nuevo valor del sensor (0-360)
         * @return Valor filtrado que considera el camino más corto
         */
        private fun lowPassFilter(
            previous: Float,
            current: Float,
        ): Float {
            // Calcular la diferencia considerando el cruce de 0°/360°
            var delta = current - previous

            // Si la diferencia es mayor a 180°, ajustar para tomar el camino más corto
            if (delta > 180) {
                delta -= 360
            } else if (delta < -180) {
                delta += 360
            }

            // Aplicar el filtro low-pass
            var result = previous + LOW_PASS_ALPHA * delta

            // Normalizar resultado a 0-360
            if (result < 0) {
                result += 360
            } else if (result >= 360) {
                result -= 360
            }

            return result
        }
    }
