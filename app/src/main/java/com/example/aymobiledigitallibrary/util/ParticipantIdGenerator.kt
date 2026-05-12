package com.example.aymobiledigitallibrary.util
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random
object ParticipantIdGenerator { fun generate(): String { val dt=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")); val c=('A'..'Z')+('0'..'9'); val r=(1..4).joinToString(""){c[Random.nextInt(c.size)].toString()}; return "P-$dt-$r" } }
