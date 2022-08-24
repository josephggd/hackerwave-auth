package com.hackerwave.auth.messaging

import com.hackerwave.auth.dto.ActionDto
import com.hackerwave.auth.dto.HistoryDto
import com.hackerwave.auth.util.CommonStrings.historyStore
import com.hackerwave.auth.util.CommonStrings.historyTopic
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
        val loginJsonSerde: JsonSerde<ActionDto> = JsonSerde<ActionDto>(ActionDto::class.java)
        val loginHistoryJsonSerde: JsonSerde<HistoryDto> = JsonSerde<HistoryDto>(HistoryDto::class.java)
        val stringLoginHistoryKStream: KStream<String, HistoryDto> = streamsBuilder.stream(
            topic,
            Consumed.with(Serdes.String(), loginJsonSerde)
        )
            .groupByKey()
            .aggregate(
                { HistoryDto() },
                { _, value, aggregate -> aggregate.aggregate(value) },
                Materialized.`as`<String, HistoryDto, KeyValueStore<Bytes, ByteArray>>(historyStore)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(loginHistoryJsonSerde)
            )
            .toStream()
        stringLoginHistoryKStream.to(historyTopic, Produced.with(Serdes.String(), loginHistoryJsonSerde))
        return streamsBuilder.build()
    }
}