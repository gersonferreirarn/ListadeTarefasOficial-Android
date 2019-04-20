package br.com.caicosoft.listadetarefasoficial.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.caicosoft.listadetarefasoficial.R;
import br.com.caicosoft.listadetarefasoficial.adapter.TarefaAdapter;
import br.com.caicosoft.listadetarefasoficial.helper.DbHelper;
import br.com.caicosoft.listadetarefasoficial.helper.RecyclerItemClickListener;
import br.com.caicosoft.listadetarefasoficial.helper.TarefaDAO;
import br.com.caicosoft.listadetarefasoficial.model.Tarefa;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvTarefas;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // configura recycler view
        rvTarefas = findViewById(R.id.rvTarefas);

        // adicionar o evento de click
        rvTarefas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        rvTarefas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                // editar

                                //recupera a tarefa para edicao
                                Tarefa tarefaSelecionada = listaTarefas.get(position);

                                //envia tarefa para a tela adicionar tarefa
                                Intent i = new Intent(MainActivity.this,AdicionarTarefaActivity.class);
                                i.putExtra("tarefaSelecionada", tarefaSelecionada);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                // deletar

                                //recupera a tarefa para exclusao
                                final Tarefa tarefaSelecionada = listaTarefas.get(position);

                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                                // configura titulo e mensagem
                                dialog.setTitle("Confirmar exclusão");
                                dialog.setMessage("Deseja excluir a tarefa: "+tarefaSelecionada.getNomeTarefa() + " ?");

                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                        if(tarefaDAO.deletar(tarefaSelecionada)){
                                            carregarListaTarefas();
                                            Toast.makeText(getApplicationContext(), "Tarefa apagada com sucesso!", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "ERRO ao apagar tarefa!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                dialog.setNegativeButton("Não", null);

                                //exibir dialog
                                dialog.create();
                                dialog.show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                startActivity(i);
            }
        });
    }

    public void carregarListaTarefas(){
        // Listar Tarefas
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();

        // Exibe Lista de Tarefas no RV

        // Configurar um Adapter
        tarefaAdapter = new TarefaAdapter(listaTarefas);

        // Configurar um Recycler View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvTarefas.setLayoutManager(layoutManager);
        rvTarefas.setHasFixedSize(true);
        rvTarefas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        rvTarefas.setAdapter(tarefaAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // carrega tarefas
        carregarListaTarefas();
    }
}
