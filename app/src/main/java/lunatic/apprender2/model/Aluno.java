package lunatic.apprender2.model;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by -Lunatic on 30/03/2016.
 */

public class Aluno extends SugarRecord {
    private String nome;
    private String serie;
    private String sexo;
    private String escola;
    private float mediaPessoal;
    private float mediaEscola;
    private List<Materia> materias = new ArrayList<Materia>();


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEscola() {
        return escola;
    }

    public void setEscola(String escola) {
        this.escola = escola;
    }

    public float getMediaPessoal() {
        return mediaPessoal;
    }

    public void setMediaPessoal(float mediaPessoal) {
        this.mediaPessoal = mediaPessoal;
    }

    public float getMediaEscola() {
        return mediaEscola;
    }

    public void setMediaEscola(float mediaEscola) {
        this.mediaEscola = mediaEscola;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    @Override
    public String toString() {
        return this.nome + " - " + this.escola;
    }

}
