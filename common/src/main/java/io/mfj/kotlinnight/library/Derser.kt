package io.mfj.kotlinnight.library

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.Serializers
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.type.SimpleType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val dateFormatter = DateTimeFormatter.BASIC_ISO_DATE

fun configuredObjectMapper() = jacksonObjectMapper().configure()

fun ObjectMapper.configure():ObjectMapper =
		apply {
			registerModule(LibraryModule)
		}

object LibraryModule:Module() {

	override fun getModuleName():String = "io.mfj.kotlinnight.library"

	override fun version():Version = Version.unknownVersion()

	override fun setupModule(context:SetupContext) {
		context.addSerializers( DateSerializer )
		context.addDeserializers( DateDeserializer )
	}

}
object DateSerializer:Serializers.Base() {
	override fun findSerializer(config:SerializationConfig, type:JavaType, beanDesc:BeanDescription):JsonSerializer<*>? =
			if ( type is SimpleType && type.rawClass == LocalDate::class.java ) {
				LocalDateSerializer
			} else {
				null
			}
}

object DateDeserializer:Deserializers.Base() {
	override fun findBeanDeserializer(type:JavaType, config:DeserializationConfig, beanDesc:BeanDescription):JsonDeserializer<*>? =
			if ( type is SimpleType && type.rawClass == LocalDate::class.java ) {
				LocalDateDeserializer
			} else {
				null
			}
}

object LocalDateDeserializer:StdDeserializer<LocalDate>(LocalDate::class.java) {
	override fun deserialize(p:JsonParser, ctxt:DeserializationContext):LocalDate {
		return LocalDate.parse( p.valueAsString, dateFormatter )
	}
}

object LocalDateSerializer:StdSerializer<LocalDate>(LocalDate::class.java) {
	override fun serialize(value:LocalDate, gen:JsonGenerator, provider:SerializerProvider) {
		gen.writeString( dateFormatter.format(value) )
	}
}
