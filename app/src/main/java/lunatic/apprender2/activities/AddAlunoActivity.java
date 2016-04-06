package lunatic.apprender2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import lunatic.apprender2.DAO.AlunoDAO;
import lunatic.apprender2.R;
import lunatic.apprender2.model.Aluno;

public class AddAlunoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aluno);
    }

    public void salvarAluno(View view){

        Aluno a = new Aluno();
        AlunoDAO dao = new AlunoDAO();

        String nomealuno = ((EditText) (findViewById(R.id.edittext_nome_aluno))).getText().toString();
        String sexo = ((EditText) (findViewById(R.id.edittext_sexo))).getText().toString();
        String escola = ((EditText) (findViewById(R.id.edittext_escola))).getText().toString();
        String serie = ((EditText) (findViewById(R.id.edittext_serie))).getText().toString();
        String me = ((EditText) (findViewById(R.id.edittext_mediaescola))).getText().toString();
        String mp = ((EditText) (findViewById(R.id.edittext_mediapessoal))).getText().toString();

        if(nomealuno.equals("")
        || sexo.equals("")
        || escola.equals("")
        || serie.equals("")
        || me.equals("")
        || mp.equals("")){
            Toast.makeText(AddAlunoActivity.this, "Complete os campos", Toast.LENGTH_SHORT).show();
        }else{

            float mediaescola = Float.parseFloat(me);
            float mediapessoal = Float.parseFloat(mp);

            if (mediaescola < mediapessoal){
                Toast.makeText(AddAlunoActivity.this, "A média pessoal deve ser maior que a da escola!", Toast.LENGTH_SHORT).show();
            }else{
                a.setNome(nomealuno);
                a.setSexo(sexo);
                a.setEscola(escola);
                a.setSerie(serie);
                a.setMediaEscola(mediaescola);
                a.setMediaPessoal(mediapessoal);
                dao.inserir(a);
            }
        }

        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
        finish();

    }

}
