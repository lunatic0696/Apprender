package lunatic.apprender2.DAO;

import com.orm.SugarRecord;

import lunatic.apprender2.model.Aluno;

/**
 * Created by -Lunatic on 31/03/2016.
 */

public class AlunoDAO{

    public void inserir(Aluno a){
        a.save();
    }

    public Aluno obter(){

        Aluno aluno = null;

        if(Aluno.findById(Aluno.class, 1) != null){
            aluno = Aluno.findById(Aluno.class, 1);
        }

        return aluno;
    }

}
