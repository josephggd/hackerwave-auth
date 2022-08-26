package com.hackerwave.auth.messaging

import com.hackerwave.auth.dto.ActionDto
import com.hackerwave.auth.dto.DateAggDto
import com.hackerwave.auth.util.MsgFunctions.determineGrouping
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

object DateAggTopology {

    fun buildTopology(inputTopic: String?, grouping:String, outputStore: String?, outputTopic: String?): Topology {
        val streamsBuilder = StreamsBuilder()
        val loginJsonSerde: JsonSerde<ActionDto> = JsonSerde<ActionDto>(ActionDto::class.java)
        val loginHistoryJsonSerde: JsonSerde<DateAggDto> = JsonSerde<DateAggDto>(DateAggDto::class.java)
        val stringLoginHistoryKStream: KStream<String, DateAggDto> = streamsBuilder.stream(
            inputTopic,
            Consumed.with(Serdes.String(), loginJsonSerde)
        )
            .groupBy { _, value -> determineGrouping(grouping, value) }
            .aggregate(
                { DateAggDto() },
                { _, value, aggregate -> aggregate.aggregate(value) },
                Materialized.`as`<String, DateAggDto, KeyValueStore<Bytes, ByteArray>>(outputStore)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(loginHistoryJsonSerde)
            )
            .toStream()
        stringLoginHistoryKStream.to(outputTopic, Produced.with(Serdes.String(), loginHistoryJsonSerde))
        return streamsBuilder.build()
    }
}