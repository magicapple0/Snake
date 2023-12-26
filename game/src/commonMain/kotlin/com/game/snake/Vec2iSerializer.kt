package com.game.snake

import com.lehaine.littlekt.math.Vec2i
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Vec2i::class)
object  Vec2iSerializer : KSerializer<Vec2i> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Vec2i"){
        element<Int>("x")
        element<Int>("y")
    }

    override fun serialize(encoder: Encoder, value: Vec2i) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.x)
            encodeIntElement(descriptor, 1, value.y)
        }
    }
    override fun deserialize(decoder: Decoder): Vec2i {
        return decoder.decodeStructure(descriptor) {
            var x: Int? = null
            var y: Int? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop

                    0 -> x = decodeIntElement(descriptor, 0)
                    1 -> y = decodeIntElement(descriptor, 1)

                }
            }

            if (x != null && y != null)
                Vec2i(x, y)
            else
                Vec2i(0, 0)
        }
    }
}