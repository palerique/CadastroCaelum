package cadastro.caelum.com.br.cadastrocaelum.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cadastro.caelum.com.br.cadastrocaelum.modelo.Aluno;

public class AlunoDAO extends SQLiteOpenHelper {

    private static final String TELEFONE = "TELEFONE";
    private static final String SITE = "SITE";
    private static final String NOTA = "NOTA";
    private static final String NOME = "NOME";
    private static final String FOTO = "FOTO";
    private static final String ENDERECO = "ENDERECO";
    private static final String ID = "ID";

    private static final int VERSAO = 1;
    private static final String TABELA_ALUNO = "alunos";

    public AlunoDAO(Context context) {
        super(context, TABELA_ALUNO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // Executado quando a tabela nao existe no android
        String sql = "CREATE TABLE %1$S (ID INTEGER PRIMARY KEY, NOME TEXT UNIQUE NOT NULL, TELEFONE TEXT, ENDERECO TEXT, SITE TEXT, NOTA REAL, FOTO TEXT);";
        db.execSQL(String.format(sql, TABELA_ALUNO));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // Executado quando a tabela ja existe no android
        String sql = "DROP TABLE IF EXISTS %1$S";
        db.execSQL(String.format(sql, TABELA_ALUNO));

        onCreate(db);
    }

    public void insert(Aluno aluno) {
        ContentValues values = toValues(aluno);
        // nullColumnRack é o valor padrao para valores nulos, no caso abaixo: null
        getWritableDatabase().insert(TABELA_ALUNO, null, values);
    }

    public void delete(Aluno aluno) {
        getWritableDatabase().delete(TABELA_ALUNO, "ID=?", new String[]{aluno.getId().toString()});
    }

    public List<Aluno> getAll() {
        List<Aluno> alunos = new ArrayList<Aluno>();
        // Não deixa utilizar multiplos cursores, apenas um por vez na aplicação
        Cursor c = getWritableDatabase().query(TABELA_ALUNO, null, null, null, null, null, "NOME ASC");
        try {
            while (c.moveToNext()) {
                Aluno aluno = new Aluno();
                aluno.setId(c.getLong(c.getColumnIndex(ID)));
                aluno.setEndereco(c.getString(c.getColumnIndex(ENDERECO)));
                aluno.setFoto(c.getString(c.getColumnIndex(FOTO)));
                aluno.setNome(c.getString(c.getColumnIndex(NOME)));
                aluno.setNota(c.getFloat(c.getColumnIndex(NOTA)));
                aluno.setSite(c.getString(c.getColumnIndex(SITE)));
                aluno.setTelefone(c.getString(c.getColumnIndex(TELEFONE)));
                alunos.add(aluno);
            }
        } finally {
            c.close();
        }
        return alunos;
    }

    private ContentValues toValues(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put(ID, aluno.getId());
        values.put(NOME, aluno.getNome());
        values.put(TELEFONE, aluno.getTelefone());
        values.put(ENDERECO, aluno.getEndereco());
        values.put(SITE, aluno.getSite());
        values.put(NOTA, aluno.getNota());
        values.put(FOTO, aluno.getFoto());
        return values;
    }

    public void update(Aluno aluno) {
        ContentValues cv = toValues(aluno);
        getWritableDatabase().update(TABELA_ALUNO, cv, "ID=?", new String[]{aluno.getId().toString()});
    }

    public void save(Aluno aluno) {
        if (aluno.getId() == null) {
            insert(aluno);
        } else {
            update(aluno);
        }
    }

    public boolean isAluno(String telefone) {
        Cursor rawQuery = getReadableDatabase().rawQuery("SELECT telefone from " + TABELA_ALUNO + " WHERE telefone like '%' ||?", new String[]{telefone});
        int total = rawQuery.getCount();
        return total > 0;
    }
}