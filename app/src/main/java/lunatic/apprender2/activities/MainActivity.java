package lunatic.apprender2.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lunatic.apprender2.DAO.AlunoDAO;
import lunatic.apprender2.DAO.DBHelper;
import lunatic.apprender2.DAO.MateriaDAO;
import lunatic.apprender2.R;
import lunatic.apprender2.model.Aluno;
import lunatic.apprender2.model.Materia;

public class MainActivity extends AppCompatActivity {

    private DBHelper helper;
    private MateriaDAO daoMateria;
    private AlunoDAO daoAluno;
    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new DBHelper(this);
        daoMateria = new MateriaDAO();
        daoAluno = new AlunoDAO();

        aluno = daoAluno.obter();

        if(aluno == null){
            Intent adicionarAluno = new Intent(this, AddAlunoActivity.class);
            startActivity(adicionarAluno);
            finish();
        }else{
            Toast.makeText(MainActivity.this, "Bem vindo(a) " + aluno.getNome(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_materia) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.title_add_materia));
            LayoutInflater inflater = LayoutInflater.from(this);
            final View dialogView = inflater.inflate(R.layout.dialog_add_materia, null);
            dialog.setView(dialogView);
            dialog.setPositiveButton(getString(R.string.dialog_option_save), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String nomemateria = ((EditText) (dialogView.findViewById(R.id.edittext_nome_materia))).getText().toString();
                    String prof = ((EditText) (dialogView.findViewById(R.id.edittext_prof))).getText().toString();

                    if(nomemateria.equals("") || prof.equals("")){
                        Toast.makeText(MainActivity.this, "Complete os campos", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        Materia m = new Materia(nomemateria, prof);
                        m.setMediaFinal(-1.0f);
                        m.setRecuperacaoFinal(-1.0f);
                        daoMateria.inserir(m);
                        onResume();
                    }

                }
            });
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarListaMaterias();
    }

    public void atualizarListaMaterias(){

        final List<Materia> lista = daoMateria.obterMaterias();
        Log.i("MainActivity", "onResume // Tamanho da lista obtida: " + lista.size());
        final ListView listviewmaterias = (ListView)findViewById(R.id.lista_materias);

        listviewmaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, BimestresActivity.class);
                Materia m = lista.get(position);
                i.putExtra("id", m.getId());
                i.putExtra("situation", m.obterSituacao(MainActivity.this, aluno));
                if(m.obterBimestresFeitos() == 4 && m.getMediaFinal() != -1.0f){
                    i.putExtra("finalizou", "sim");
                }else{
                    i.putExtra("finalizou", "não");
                }
                startActivity(i);
            }
        });

        listviewmaterias.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(getString(R.string.dialog_deletar_materia));
                dialog.setPositiveButton(getString(R.string.dialog_option_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        daoMateria.deletar(lista.get(position).getId());
                        atualizarListaMaterias();
                    }
                });
                dialog.setNegativeButton(getString(R.string.dialog_option_no), null);
                dialog.show();
                return true;
            }
        });

        Log.i("MainActivity", "atualizarLista // Tamanho da lista obtida: " + lista.size());
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, lista){
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

                TextView firstLine = (TextView) view.findViewById(android.R.id.text1);
                TextView secondLine = (TextView) view.findViewById(android.R.id.text2);

                firstLine.setTextSize(20);
                secondLine.setTextSize(18);

                Materia m = lista.get(position);

                float mediaAtual = m.calcularMedia();
                float mediaFinal = m.getMediaFinal();

                if(mediaFinal != -1.0f){
                    mediaAtual = mediaFinal;
                }

                String situacao = m.obterSituacao(MainActivity.this, aluno);

                firstLine.setText(m.getNomeMateria() + " - " + m.getProfessor());
                secondLine.setText("Média atual: " + mediaAtual + "\n" + situacao);

                return view;
            }
        };

        listviewmaterias.setAdapter(adapter);
    }

}
