package com.example.democassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.example.democassandra.entities.Vet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;

public class DemocassandraApplication {
    private final static Logger log = LoggerFactory.getLogger(DemocassandraApplication.class);


    private static Vet newVet(String nome, int idade) throws UnknownHostException {
        return new Vet(UUID.randomUUID().toString(), InetAddress.getLocalHost().getCanonicalHostName(), nome, idade);
    }

    public static void main(String[] args) throws UnknownHostException {
        CqlSession cqlSession = CqlSession.builder()
                .withKeyspace("spring_cassandra")
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .build();

        CassandraOperations template = new CassandraTemplate(cqlSession);
//
        Vet luna = template.insert(newVet("Luna", 2));
        //Primeira forma de fazer um insert
        template.insert(newVet("Luke", 3));
        template.insert(newVet("Nemesis", 4));
        template.insert(newVet("Bibi", 5));

        //Segunda forma de fazer um insert
        Vet luki = new Vet(UUID.randomUUID().toString(), InetAddress.getLocalHost().getHostName(), "Luki", 30);

        template.insert(luki);

        //Terceira forma de fazer um insert
        Vet donaGorda = new Vet(UUID.randomUUID().toString(), InetAddress.getLocalHost().getHostName(), "DonaGorda", 30);
        String insetString = String.format("INSERT INTO %s (id, data, idade, nome, nome_node) VALUES (?,?,?,?,?) ", "vet");
        PreparedStatement preparedInsert = cqlSession.prepare(insetString);
        BoundStatement boundStatement = preparedInsert.bind(donaGorda.getId(), donaGorda.getData(), donaGorda.getIdade(), donaGorda.getNome(), donaGorda.getNome_node());
        cqlSession.execute(boundStatement);


//        //Quarta forma e tread
//        try {
//
//            long start = System.currentTimeMillis();
//            Thread[] threads = new Thread[10];
//            for (int i = 0; i < threads.length; i++) {
//                threads[i] = new Thread(() -> {
//                    for (int j = 0; j < 100000; j++) {
//                        try {
//                            SimpleStatement statement = SimpleStatement.builder("INSERT INTO vet (id, data, idade, nome, nome_node) VALUES (?, toTimestamp(now()),?,?,?) ")
//                                    .addPositionalValues(UUID.randomUUID().toString())
//                                    .addPositionalValues(j)
//                                    .addPositionalValues("NOME " + j)
//                                    .addPositionalValues(InetAddress.getLocalHost().getHostName())
//                                    .build();
//                            cqlSession.execute(statement);
//                        } catch (UnknownHostException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                });
//                threads[i].start();
//            }
//
//            for (Thread thread : threads) {
//                thread.join();
//            }
//            long end = System.currentTimeMillis();
//            log.info("Tempo: " + (end - start));
//            log.info("inserção concluida " + threads.length + " threads");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        log.info(template.selectOne(Query.query(Criteria.where("id").is(luna.getId())), Vet.class).getId());

        String colName = "nome";
        String colValue = "Luna";

//        Query insertVetQuery = Query.query()

        long start = System.currentTimeMillis();
        Query query = Query.query(Criteria.where(colName).is(colValue)).withAllowFiltering();
        List<Vet> response = template.select(query, Vet.class);
        for (Vet entity : response) {
            System.out.println(entity);
        }
        long end = System.currentTimeMillis();
        log.info("Tempo: " + (end - start));





//        template.truncate(Vet.class);
        cqlSession.close();
    }


}
