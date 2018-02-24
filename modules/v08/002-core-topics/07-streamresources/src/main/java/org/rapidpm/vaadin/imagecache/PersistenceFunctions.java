package org.rapidpm.vaadin.imagecache;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.rapidpm.frp.model.Pair;

import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 *
 */
public interface PersistenceFunctions {


  public static class DatabasePair extends Pair<DB, DB> {

    public DatabasePair(DB db, DB db2) {
      super(db, db2);
    }

    public DB memoryDB() {
      return this.getT1();
    }

    public DB onDiscDB() {
      return this.getT2();
    }
  }


  public static Function<String, DatabasePair> cachingDB() {
    return (name) -> {
      final File databaseFile = new File("target", name);

      final DB dbDisk = DBMaker
          .fileDB(databaseFile)
          .closeOnJvmShutdown()
          .concurrencyScale(10)
          .fileMmapEnableIfSupported()
          .make();

      final DB dbMemory = DBMaker
          .memoryDB()
          .closeOnJvmShutdown()
          .make();
      return new DatabasePair(dbMemory, dbDisk);
    };
  }


  public static BiFunction<DatabasePair, String, HTreeMap<String, byte[]>> mapOnDisc() {
    return (database, name) -> database.getT2()
                                       .hashMap(name + "_onDisc", Serializer.STRING, Serializer.BYTE_ARRAY)
                                       .expireCompactThreshold(0.4) //40%
                                       .createOrOpen();
  }

  public static BiFunction<DatabasePair, String, HTreeMap<String, byte[]>> mapInMemoryPersistentOnDisc() {
    return (database, name) -> {

      final HTreeMap<String, byte[]> overflowMap = mapOnDisc().apply(database, name);
      return database.getT1()
                     .hashMap(name + "_inMemory", Serializer.STRING, Serializer.BYTE_ARRAY)
                     .expireAfterCreate()
                     .expireAfterUpdate()
                     .expireOverflow(overflowMap)
                     .expireExecutor(newScheduledThreadPool(2))
                     .createOrOpen();

    };
  }

}
