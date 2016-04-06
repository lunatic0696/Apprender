package lunatic.apprender2.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by -Lunatic on 31/03/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Apprender.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Materias " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "prof TEXT," +
                "mediageral REAL);";

        db.execSQL(sql);

        sql = "CREATE TABLE Bimestres " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "numero INTEGER," +
                "nota1 REAL," + "nota2 REAL," + "recuperacao REAL," + "media REAL," +
                "id_materia INTEGER);";

        db.execSQL(sql);

        sql = "CREATE TABLE Aluno " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nomeAluno TEXT," + "sexo TEXT," + "escola TEXT," + "serie TEXT," +  "mediaEscola REAL," +
                "mediaPessoal REAL);";

        db.execSQL(sql);

        Log.i("DBHelper", "Tabelas criadas");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
