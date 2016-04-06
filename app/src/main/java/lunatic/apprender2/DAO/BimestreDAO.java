package lunatic.apprender2.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.orm.SugarDb;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import lunatic.apprender2.model.Bimestre;
import lunatic.apprender2.model.Materia;

/**
 * Created by -Lunatic on 31/03/2016.
 */
public class BimestreDAO {

    public void inserir(Bimestre b){
        b.save();
    }

    public void adicionarNota(Bimestre bimestre, int tipo, float nota){
        Bimestre b = Bimestre.findById(Bimestre.class, bimestre.getId());
        Log.i("BimestreDAO", "Bimestre " + b.getNumero() + " obtido // Notas: " + b.obterNotas().toString());
        switch (tipo){
            case 0:
                b.setNota1(nota);
                break;
            case 1:
                b.setNota2(nota);
                break;
            case 2:
                b.setRecuperacao(nota);
                break;
            case 3:
                b.setMedia(nota);
                break;
        }
        Log.i("BimestreDAO", "Bimestre " + b.getNumero() + " modificado // Notas: " + b.obterNotas().toString());
        b.save();
    }

    public List<Bimestre> obterBimestres(Materia m){ //Puxa uma lista de bimestre para colocar na listview da materia activity
        List<Bimestre> bimestres = Bimestre.find(Bimestre.class, "materia = ?", String.valueOf(m.getId()));
        return bimestres;
    }

}
