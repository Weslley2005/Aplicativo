package br.unigran.aplicativo.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.unigran.aplicativo.model.Aplicativo;

public class AplicativoImplBD implements AplicativoDao {
    DBHelper banco;

    public AplicativoImplBD(Context context) {
        this.banco = new DBHelper(context);
    }

    @Override
    public void salvar(Aplicativo a) {

        ContentValues valores = new ContentValues();
        valores.put("nome", a.getNome());
        valores.put("data", a.getData());
        valores.put("categoria", a.getCategoria());

        banco.getWritableDatabase().insertOrThrow("aplicativo", null, valores);
        banco.close();

    }

    @Override
    public void editar(Aplicativo a) {
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nome", a.getNome());
        valores.put("data", a.getData());
        valores.put("categoria", a.getCategoria());

        try {
            db.update("aplicativo", valores, "id=?", new String[]{String.valueOf(a.getId())});
        } catch (SQLException e) {
        } finally {
            db.close();
        }
    }

    @Override
    public void remove(Aplicativo a) {
        SQLiteDatabase db = banco.getWritableDatabase();

        try {
            db.delete("atividade", "id=?", new String[]{String.valueOf(a.getId())});
        } catch (SQLException e) {
        } finally {
            db.close();
        }
    }

    @Override
    public List<Aplicativo> listagem() {
        List<Aplicativo> retorno = new ArrayList<>();
        SQLiteDatabase db = banco.getReadableDatabase();
        String colunas[] = {"id", "nome", "data", "categoria"};

        try {
            Cursor cursor = db.query("aplicativo", colunas, null, null, null, null, "nome");
            final int COLUMN_ID = 0, COLUMN_NOME = 1, COLUMN_DATA = 2, COLUMN_CATEGORIA = 3;

            while (cursor.moveToNext()) {
                Aplicativo a = new Aplicativo();
                a.setId(cursor.getInt(COLUMN_ID));
                a.setNome(cursor.getString(COLUMN_NOME));
                a.setData(cursor.getString(COLUMN_DATA));
                a.setCategoria(cursor.getString(COLUMN_CATEGORIA));


                retorno.add(a);
            }
        } catch (SQLException e) {

        } finally {
            db.close();
        }

        return retorno;
    }
}
