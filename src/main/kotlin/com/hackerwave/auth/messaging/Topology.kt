package com.hackerwave.auth.messaging

import com.hackerwave.auth.dto.LoginDto
import com.hackerwave.auth.dto.LoginHistoryDto
import com.hackerwave.auth.util.CommonStrings.loginHistoryStore
import com.hackerwave.auth.util.CommonStrings.loginHistoryTopic
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.state.KeyValueStore
import org.springframework.kafka.support.serializer.JsonSerde

object StreamTopology {
    fun buildTopology(topic: String?): Topology {
        val streamsBuilder = StreamsBuilder()
        val loginJsonSerde: JsonSerde<LoginDto> = JsonSerde<LoginDto>(LoginDto::class.java)
        val loginHistoryJsonSerde: JsonSerde<LoginHistoryDto> = JsonSerde<LoginHistoryDto>(LoginHistoryDto::class.java)
        val stringLoginHistoryKStream: KStream<String, LoginHistoryDto> = streamsBuilder.stream(
            topic,
            Consumed.with(Serdes.String(), loginJsonSerde)
        )
            .groupByKey()
            .aggregate(
                { LoginHistoryDto() },
                { _, value, aggregate -> aggregate.aggregate(value) },
                Materialized.`as`<String, LoginHistoryDto, KeyValueStore<Bytes, ByteArray>>(loginHistoryStore)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(loginHistoryJsonSerde)
            )
            .toStream()
        stringLoginHistoryKStream.to(loginHistoryTopic, Produced.with(Serdes.String(), loginHistoryJsonSerde))
        return streamsBuilder.build()
    }
}