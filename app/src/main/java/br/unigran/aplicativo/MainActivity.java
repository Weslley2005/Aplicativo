package br.unigran.aplicativo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.unigran.aplicativo.model.Aplicativo;
import br.unigran.aplicativo.persistence.AplicativoDao;
import br.unigran.aplicativo.persistence.AplicativoImplBD;

public class MainActivity extends AppCompatActivity {
    private EditText nome;
    private DatePicker data;
    private Spinner categoria;
    private Button botao;
    private ListView listagem;
    private List<Aplicativo> dados;
    private AplicativoDao dao;
    private Aplicativo a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapeamentoXML();
        consulta();
        vinculaAdapterALista();
        acoes();

        categoria.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.planets_array,
                android.R.layout.simple_spinner_item));

        ArrayList<String> strings = new ArrayList<>();
        strings.add("Trabalho");
        strings.add("Lazer");
        strings.add("Estudo");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strings);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(spinnerAdapter);
    }

    private void mapeamentoXML() {
        nome = findViewById(R.id.idNome);
        data = findViewById(R.id.idData);
        categoria = findViewById(R.id.idCategoria);
        botao = findViewById(R.id.idAdd);
        listagem = findViewById(R.id.idListagem);
    }

    private void consulta() {
        if (dao == null)
            dao = new AplicativoImplBD(this);
        dados = dao.listagem();
    }

    private void vinculaAdapterALista() {
        listagem.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dados));
    }

    private void acoes() {
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a == null)
                    a = new Aplicativo();

                a.setNome(nome.getText().toString());
                a.setData(data.getDayOfMonth() + "/" + (data.getMonth() + 1) + "/" + data.getYear());
                a.setCategoria(categoria.getSelectedItem().toString());

                if (a.getId() == null)
                    dao.salvar(a);
                else
                    dao.editar(a);
                limpaCampos();
                atualizaDados();
            }
        });

        listagem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int indice, long l) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Realmente deseja excluir?")
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dao.remove(dados.get(indice));
                                atualizaDados();
                            }
                        })
                        .create()
                        .show();

                return true;
            }
        });

        listagem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                a = dados.get(i);
                nome.setText(a.getNome());
                categoria.setSelection(getCategoriaPosition(a.getCategoria()));
            }
        });
    }

    private int getCategoriaPosition(String categoriaSelecionada) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) categoria.getAdapter();
        return adapter.getPosition(categoriaSelecionada);
    }

    private void atualizaDados() {
        dados.clear();
        dados.addAll(dao.listagem());
        ((ArrayAdapter<Aplicativo>) listagem.getAdapter()).notifyDataSetChanged();
    }

    private void limpaCampos() {
        nome.setText("");
        categoria.setSelection(0);

        a = null;
    }

    public void cancelar(View view) {
        AlertDialog.Builder acao = new AlertDialog.Builder(this);
        acao.setTitle("Você quer sair");
        acao.setItems(new CharSequence[]{"Sair"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        acao.create().show();
    }
}
