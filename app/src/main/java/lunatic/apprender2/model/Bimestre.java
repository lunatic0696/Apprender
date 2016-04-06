package lunatic.apprender2.model;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by -Lunatic on 30/03/2016.
 */

public class Bimestre extends SugarRecord {

    private int numero;
    private float nota1;
    private float nota2;
    private float recuperacao;
    private float media;
    private Materia materia;

    public Bimestre() {

    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public float getNota1() {
        return nota1;
    }

    public void setNota1(float nota1) {
        this.nota1 = nota1;
        Log.i("Bimestre", "Nota1 setada para: " + this.nota1);
    }

    public float getNota2() {
        return nota2;
    }

    public void setNota2(float nota2) {
        this.nota2 = nota2;
    }

    public float getRecuperacao() {
        return recuperacao;
    }

    public void setRecuperacao(float recuperacao) {
        this.recuperacao = recuperacao;
    }

    public float getMedia() {
        return media;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public List<Float> obterNotas(){
        List<Float> listaNotas = new ArrayList<>();
        listaNotas.add(nota1);
        listaNotas.add(nota2);
        listaNotas.add(recuperacao);
        listaNotas.add(media);
        return listaNotas;

    }
}
