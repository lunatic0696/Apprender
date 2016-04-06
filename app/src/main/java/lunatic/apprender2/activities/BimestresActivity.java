package lunatic.apprender2.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lunatic.apprender2.DAO.AlunoDAO;
import lunatic.apprender2.DAO.BimestreDAO;
import lunatic.apprender2.DAO.DBHelper;
import lunatic.apprender2.DAO.MateriaDAO;
import lunatic.apprender2.R;
import lunatic.apprender2.extra.ExpandableListView;
import lunatic.apprender2.model.Aluno;
import lunatic.apprender2.model.Bimestre;
import lunatic.apprender2.model.Materia;

public class BimestresActivity extends AppCompatActivity {

    private Materia materiaAtual;
    private BimestreDAO daoBimestre;
    private MateriaDAO daoMateria;
    private AlunoDAO daoAluno;
    private Aluno aluno;

    private int[] bimestresListView = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bimestres);

        daoBimestre = new BimestreDAO();
        daoMateria = new MateriaDAO();
        daoAluno = new AlunoDAO();
        aluno = daoAluno.obter();

        final float mediaAluno = aluno.getMediaPessoal();

        materiaAtual = daoMateria.obter(getIntent().getLongExtra("id", 0));

        for (int i = 1; i < 5; i++){
            bimestresListView[i] = getResources().getIdentifier("lista_notas" + i, "id", getPackageName());
            //
            final int finalI = i;
            ExpandableListView lv = (ExpandableListView) findViewById(bimestresListView[i]);
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    final List<Bimestre> ln = daoBimestre.obterBimestres(materiaAtual);
                    final Bimestre bimestre = ln.get((finalI) - 1);

                    if (bimestre.getMedia() >= mediaAluno && position == 2 || position == 3) {
                        Toast.makeText(BimestresActivity.this, "Esta nota não é editável", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(BimestresActivity.this);
                        dialog.setTitle(getString(R.string.title_add_nota));
                        LayoutInflater inflater = LayoutInflater.from(BimestresActivity.this);
                        View dialogView = inflater.inflate(R.layout.dialog_add_nota, null);
                        final EditText edt = (EditText) dialogView.findViewById(R.id.edt_add_nota);
                        edt.setText(String.valueOf(aluno.getMediaPessoal()));
                        dialog.setView(dialogView);
                        dialog.setPositiveButton(getString(R.string.dialog_option_save), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                float notaAInserir = -1.0f;

                                if (!edt.getText().toString().equals("")){
                                    notaAInserir = Float.parseFloat(edt.getText().toString());
                                    if (notaAInserir >= 10.0f) {
                                        dialog.dismiss();
                                        Toast.makeText(BimestresActivity.this, "Adicione uma nota menor que dez", Toast.LENGTH_SHORT).show();}
                                }

                                switch (position) {
                                    case 2:
                                        if (bimestre.getMedia() < mediaAluno) {
                                            bimestre.setRecuperacao(notaAInserir);
                                            daoBimestre.adicionarNota(bimestre, 2, notaAInserir);
                                        } else {
                                            Toast.makeText(BimestresActivity.this, "Você ainda não pode inserir a prova final/recuperação!", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    default:
                                        if (position == 0) {
                                            bimestre.setNota1(notaAInserir);
                                        } else {
                                            bimestre.setNota2(notaAInserir);
                                        }
                                        daoBimestre.adicionarNota(bimestre, position, notaAInserir);
                                        break;
                                }

                                float nota1 = bimestre.getNota1();
                                float nota2 = bimestre.getNota2();
                                float recuperacao = bimestre.getRecuperacao();
                                float media = bimestre.getMedia();

                                if (nota1 != -1.0f && nota2 != -1.0f && recuperacao == -1.0f) {
                                    media = (nota1 + nota2) / 2;

                                    if (media < mediaAluno) {
                                        Snackbar.make(findViewById(R.id.activity_bimestres), "Você ficou com a média baixa, adicione a prova final!", Snackbar.LENGTH_LONG).show();
                                    } else if (media >= mediaAluno) {
                                        Snackbar.make(findViewById(R.id.activity_bimestres), "Sua média ficou boa neste bimestre!", Snackbar.LENGTH_LONG).show();
                                    }

                                    if (bimestre.getNumero() == 4) {
                                        daoMateria.inserirNotaFinal(media, false, materiaAtual);
                                    }

                                } else if (recuperacao != -1.0f) {
                                    media = (recuperacao + media) / 2;

                                    if (media < mediaAluno) {
                                        Snackbar.make(findViewById(R.id.activity_bimestres), "Sua média ficou baixa neste bimestre...", Snackbar.LENGTH_LONG).show();
                                    } else if (media >= mediaAluno) {
                                        Snackbar.make(findViewById(R.id.activity_bimestres), "Sua média ficou boa neste bimestre!", Snackbar.LENGTH_LONG).show();
                                    }

                                    if (bimestre.getNumero() == 4) {
                                        daoMateria.inserirNotaFinal(media, false, materiaAtual);
                                    }
                                }

                                bimestre.setMedia(media);
                                daoBimestre.adicionarNota(bimestre, 3, media);

                                atualizarListaBimestres(bimestre);

                            }
                        });
                        dialog.show();
                    }
                    return true;
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final List<Bimestre> lista = daoBimestre.obterBimestres(materiaAtual);
        for (int i = 0; i < 4; i++) {
            atualizarListaBimestres(lista.get(i));
        }
    }

    public void atualizarListaBimestres(Bimestre b){
        final List<Float> listaNotas = b.obterNotas();
        Log.i("Bimestre " + b.getNumero(), "Notas obtidas: " + listaNotas.toString());
        final ExpandableListView lv = (ExpandableListView) findViewById(bimestresListView[b.getNumero()]);
        final String[] nomesNotas = getResources().getStringArray(R.array.nomes_notas);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, listaNotas){
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

                TextView firstLine = (TextView) view.findViewById(android.R.id.text1);
                TextView secondLine = (TextView) view.findViewById(android.R.id.text2);

                firstLine.setTextSize(16);
                firstLine.setText(nomesNotas[position]);

                secondLine.setTextSize(20);

                if (listaNotas.get(position) == -1.0f){
                    secondLine.setText("-");
                }else{
                    secondLine.setText(String.valueOf(listaNotas.get(position)));
                }

                return view;
            }
        };

        lv.setAdapter(adapter);
        lv.setExpanded(true);

        Snackbar s = null;
        String texto = "";

        if(getIntent().getStringExtra("finalizou").equals("sim")) {
            if (materiaAtual.getRecuperacaoFinal() != -1.0 && materiaAtual.getMediaFinal() >= aluno.getMediaPessoal()) {
                texto = "Você passou!\nMédia final: " + materiaAtual.getMediaFinal() + " / Nota da recuperação: " + materiaAtual.getRecuperacaoFinal();
            } else if (materiaAtual.getRecuperacaoFinal() != -1.0 && materiaAtual.getMediaFinal() < aluno.getMediaPessoal()) {
                texto = "Você não passou...\nMédia final: " + materiaAtual.getMediaFinal() + " / Nota da recuperação: " + materiaAtual.getRecuperacaoFinal();
            } else if (materiaAtual.getRecuperacaoFinal() == -1.0 && materiaAtual.getMediaFinal() >= aluno.getMediaPessoal()) {
                texto = "Você passou!\nMédia final: " + materiaAtual.getMediaFinal();
            } else if (materiaAtual.getRecuperacaoFinal() == -1.0 && materiaAtual.getMediaFinal() < aluno.getMediaPessoal()) {
                texto = "Você não passou...\nMédia final: " + materiaAtual.getMediaFinal();
            }

            s = Snackbar.make(findViewById(R.id.activity_bimestres), texto, Snackbar.LENGTH_INDEFINITE);
            s.show();
        }

        String situacao = getIntent().getStringExtra("situation");
        Log.i("BimestresActivity", "Situação obtida: " + situacao);
        if(situacao.equals(getString(R.string.situation_prova_final))){

            if(s != null && s.isShown()){
                s.dismiss();
            }

            s = Snackbar.make(findViewById(R.id.activity_bimestres), "Você ficou de prova final", Snackbar.LENGTH_INDEFINITE);
            s.setAction("Adicionar nota", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(BimestresActivity.this);
                    dialog.setTitle(getString(R.string.title_add_nota));
                    LayoutInflater inflater = LayoutInflater.from(BimestresActivity.this);
                    View dialogView = inflater.inflate(R.layout.dialog_add_nota, null);
                    final EditText edt = (EditText) dialogView.findViewById(R.id.edt_add_nota);
                    edt.setText(String.valueOf(aluno.getMediaPessoal()));
                    dialog.setView(dialogView);

                    dialog.setPositiveButton(getString(R.string.dialog_option_save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            float nota = -1.0f;
                            if (!edt.getText().toString().equals(""))
                                nota = Float.parseFloat(edt.getText().toString());
                                if(nota >= 10.0f){
                                    dialog.dismiss();
                                    Toast.makeText(BimestresActivity.this, "Adicione uma nota menor que dez", Toast.LENGTH_SHORT).show();}
                            daoMateria.inserirNotaFinal(nota, true, materiaAtual);
                            materiaAtual.calcularMedia();
                            Log.i("BimestresActivity", "SITUAÇÃO: médiaFinal: " + materiaAtual.getMediaFinal() + " // recup: " + nota);
                            float mediaFinal = (materiaAtual.getMediaFinal() + nota) / 2;
                            Log.i("BimestresActivity", "Nota final: " + mediaFinal);
                            daoMateria.inserirNotaFinal(mediaFinal, false, materiaAtual);
                            finish();
                        }
                    });
                    dialog.show();

                }
            });
            s.show();
        }




    }
}
