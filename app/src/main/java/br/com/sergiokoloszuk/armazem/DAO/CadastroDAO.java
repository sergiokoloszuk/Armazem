package br.com.sergiokoloszuk.armazem.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.sergiokoloszuk.armazem.model.Cadastro;

@Dao
public interface CadastroDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cadastro cadastro);

    @Update()
    void update(Cadastro cadastro);

    @Delete
    void delete(Cadastro cadastro);

    @Query("SELECT * from cadastro ORDER BY app ASC")
    LiveData<List<Cadastro>> getAll();

    @Query("SELECT * from cadastro WHERE id = :id")
    Cadastro getByID(long id);

    @Query("SELECT * from cadastro WHERE app = :app")
    Cadastro getByApp(String app);
}
