package lunatic.apprender2.model;

import android.content.Context;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.List;

import lunatic.apprender2.R;

/**
 * Created by -Lunatic on 30/03/2016.
 */

public class Materia extends SugarRecord {

    private String nomeMateria;
    private String professor;
    private float recuperacaoFinal;
    private float mediaFinal;

    @Ignore
    private List<Bimestre> bimestres = new ArrayList<>();

    public String obterSituacao(Context context, Aluno aluno){
        Log.i("SITUAÇÃO", "mediaFinal = " + mediaFinal + " //  recup = " + recuperacaoFinal);
        String situacao = "";
        float mediaAluno = aluno.getMediaPessoal();
        if (mediaFinal < mediaAluno && mediaFinal != -1.0f){
            if(recuperacaoFinal == -1.0f && obterBimestresFeitos() == 4){
                situacao += context.getString(R.string.situation_prova_final);
            }else if(recuperacaoFinal != -1.0f && obterBimestresFeitos() == 4){
                situacao += context.getString(R.string.situation_reprovou);
            }
        }else if(mediaFinal >= mediaAluno){
            if(obterBimestresFeitos() < 4){
                situacao += context.getString(R.string.situation_bom);
            }
            else{
                situacao += context.getString(R.string.situaion_passou);
            }

        }
        return situacao;

    }


    public float calcularMedia(){

        int contador = 0;
        float mediaFinal = 0.0f;

        for (int i = 0; i < bimestres.size(); i++) {
            if(bimestres.get(i).getMedia() != -1.0f){
                mediaFinal += bimestres.get(i).getMedia();
                contador++;
            }
        }

        Log.i("MATERIA", "calcularMedia: " + mediaFinal + " // C: " + contador);

        return mediaFinal/contador;

    }

    public int obterBimestresFeitos(){

        int contador = 0;

        for (int i = 0; i < bimestres.size(); i++) {
            if(bimestres.get(i).getMedia() != -1.0f){
                contador++;
            }
        }

        return contador;

    }

    public Materia(String nomeMateria, String professor) {
        this.nomeMateria = nomeMateria;
        this.professor = professor;
    }

    public Materia() {
    }

    public String getNomeMateria() {
        return nomeMateria;
    }

    public void setNomeMateria(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public List<Bimestre> getBimestres() {
        return bimestres;
    }

    public void setBimestres(List<Bimestre> bimestres) {
        this.bimestres = bimestres;
    }

    public float getRecuperacaoFinal() {
        return recuperacaoFinal;
    }

    public void setRecuperacaoFinal(float recuperacaoFinal) {
        this.recuperacaoFinal = recuperacaoFinal;
    }
    public float getMediaFinal() {
        return mediaFinal;
    }

    public void setMediaFinal(float mediaFinal) {
        this.mediaFinal = mediaFinal;
    }
}
