package br.com.sergiokoloszuk.armazem.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.arch.persistence.room.RoomDatabase;

import br.com.sergiokoloszuk.armazem.DAO.CadastroDAO;
import br.com.sergiokoloszuk.armazem.model.Cadastro;



@Database(entities = {Cadastro.class}, version = 1)
public abstract class DatabaseRoom extends RoomDatabase {
    public abstract CadastroDAO cadastroDAO();
    private static volatile DatabaseRoom INSTANCE;

    public static DatabaseRoom getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseRoom.class, "sqlite_room_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
