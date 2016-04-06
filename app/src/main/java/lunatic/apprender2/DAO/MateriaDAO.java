package lunatic.apprender2.DAO;

import android.content.ContentValues;
import android.content.Context;
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
public class MateriaDAO {

    public void inserir(Materia materia) {
        materia.save();
        for (int i = 1; i < 5; i++) {
            Bimestre b = new Bimestre();
            b.setNumero(i);
            b.setMedia(-1.0f);
            b.setNota1(-1.0f);
            b.setNota2(-1.0f);
            b.setRecuperacao(-1.0f);
            b.setMateria(materia);
            b.save();
        }
    }


    public List<Materia> obterMaterias(){     //Puxa uma lista de matérias para colocar na listview da main activity
        List<Materia> lm = Materia.listAll(Materia.class);
        BimestreDAO dao = new BimestreDAO();
        for (int i = 0; i < lm.size(); i++) {
            lm.get(i).setBimestres(dao.obterBimestres(lm.get(i)));
        }
        return lm;
    }

    public Materia obter(long id){
        Materia m = Materia.findById(Materia.class, id);
        BimestreDAO dao = new BimestreDAO();
        m.setBimestres(dao.obterBimestres(m));
        return m;
    }

    public void deletar(long id){
        Materia.deleteAll(Materia.class, "id = ?", String.valueOf(id));
    }

    public void inserirNotaFinal(float nota, boolean recuperacao, Materia m){
        Materia materia = obter(m.getId());
        if(recuperacao){
            materia.setRecuperacaoFinal(nota);
        }else {
            materia.setMediaFinal(nota);
        }
        materia.save();
    }

}
