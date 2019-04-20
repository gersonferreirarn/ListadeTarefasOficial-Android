package br.com.caicosoft.listadetarefasoficial.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import br.com.caicosoft.listadetarefasoficial.R;
import br.com.caicosoft.listadetarefasoficial.helper.TarefaDAO;
import br.com.caicosoft.listadetarefasoficial.model.Tarefa;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private EditText etTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        etTarefa = findViewById(R.id.etTarefa);

        //recupera tarefa, em caso de edição
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //configura tarefa na caixa de texto
        if(tarefaAtual != null){
            etTarefa.setText(tarefaAtual.getNomeTarefa());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemSalvar:

                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                if(tarefaAtual == null){ // CADASTRO
                    // Executa ação para item salvar
                    String nomeTarefa = etTarefa.getText().toString();

                    if(!nomeTarefa.isEmpty()) { // SO SALVA QUANDO TIVER CONTEUDO

                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);

                        if(tarefaDAO.salvar(tarefa)){
                            Toast.makeText(getApplicationContext(), "Adicionada com sucesso!", Toast.LENGTH_LONG).show();
                            finish(); // fecha tela
                        }else{
                            Toast.makeText(getApplicationContext(), "ERRO ao salvar tarefa!", Toast.LENGTH_LONG).show();

                        }
                    }
                }else{ // EDIÇÃO
                    String nomeTarefa = etTarefa.getText().toString();

                    if(!nomeTarefa.isEmpty()) { // SO SALVA QUANDO TIVER CONTEUDO
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setId(tarefaAtual.getId());

                        //atualiza no banco de dados
                        if(tarefaDAO.atualizar(tarefa)){
                            Toast.makeText(getApplicationContext(), "Atualização feita com sucesso!", Toast.LENGTH_LONG).show();
                            finish(); // fecha tela
                        }else{
                            Toast.makeText(getApplicationContext(), "ERRO ao atualizar tarefa!", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
