/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.plugin.example;

import com.google.common.collect.ImmutableMap;
import io.trino.plugin.kafka.KafkaPlugin;
import io.trino.testing.DistributedQueryRunner;
import io.trino.testing.QueryRunner;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;

import java.util.Map;

import static io.trino.testing.TestingSession.testSessionBuilder;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;


public class SftpPluginTest
{


    public static synchronized QueryRunner createQueryRunner(Map<String, String> extraProperties)
            throws Exception
    {
        QueryRunner queryRunner = DistributedQueryRunner.builder(
                testSessionBuilder()
                        .setCatalog("sftp")
                        .setSchema("ccr").build())
                .setExtraProperties(extraProperties)
                .setNodeCount(1)
                .build();

        queryRunner.installPlugin(new SftpPlugin());
        queryRunner.installPlugin(new KafkaPlugin());
        queryRunner.createCatalog("sftp", "sftp",
                ImmutableMap.of("metadata-uri", "file:///Users/simon/workspace/trino.git/etc/sftp-metadata.json"));




        queryRunner.createCatalog("kafka", "kafka",
                ImmutableMap.of("kafka.table-names", "customer",
                        "kafka.nodes", "10.194.188.93:30355",
                        "kafka.config.resources", "/Users/simon/workspace/trino.git/etc/catalog/kafka-conf.properties",
                        "kafka.table-description-dir", "/Users/simon/workspace/trino.git/etc/kafka"
                ));


        return queryRunner;
    }

    public static void main(String[] args)
            throws Exception
    {

        QueryRunner  queryRunner = createQueryRunner(
                ImmutableMap.of("http-server.http.port", "9999",
                "node-scheduler.include-coordinator","true"));

        queryRunner.execute("select count(*) from sftp.ccr.customer limit 10").iterator().forEachRemaining(materializedRow ->
                materializedRow.getFields().iterator().forEachRemaining(o -> System.out.println(o)));

    }
}
