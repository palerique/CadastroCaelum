package cadastro.caelum.com.br.cadastrocaelum;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cadastro.caelum.com.br.cadastrocaelum.adapters.ListaAlunosAdapter;
import cadastro.caelum.com.br.cadastrocaelum.dao.AlunoDAO;
import cadastro.caelum.com.br.cadastrocaelum.modelo.Aluno;


public class ListaAlunos extends Activity {

    private ListView lista;
    private ListaAlunosAdapter adapter;
    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_alunos);

        lista = (ListView) findViewById(R.id.lista_alunos);

        registerForContextMenu(lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                aluno = (Aluno) adapter.getItem(position);

                Intent carregaFormulario = new Intent(ListaAlunos.this, Formulario.class);

                carregaFormulario.putExtra(Extras.ALUNO_SELECIONADO, aluno);

                startActivity(carregaFormulario);

                Toast.makeText(ListaAlunos.this, "Posição selecionada:" + position, Toast.LENGTH_SHORT).show();

            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ListaAlunos.this, "Clique longo", Toast.LENGTH_SHORT).show();

                aluno = (Aluno) adapter.getItem(position);

                return false;
            }
        });
    }

    @Override
    protected void onResume() {

        carregaLista();

        super.onResume();
    }

    private void carregaLista() {
        AlunoDAO alunoDAO = new AlunoDAO(this);

        List<Aluno> alunos = alunoDAO.getAll();
        alunoDAO.close();

        //String[] alunos = {"Anderson", "Filipe", "Guilherme", "Paulo Henrique"};

//        adapter = new ArrayAdapter<Aluno>(this, R.layout.linha_listagem, alunos);
        adapter = new ListaAlunosAdapter(alunos, this);

        lista.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_lista_alunos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_novo:
                Intent intentIrParaForm = new Intent(this, Formulario.class);
                startActivity(intentIrParaForm);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuItem ligar = menu.add("Ligar");
        Intent intentLigar = new Intent(Intent.ACTION_CALL);
        intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
        ligar.setIntent(intentLigar);

        MenuItem sms = menu.add("Enviar SMS");
        Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSms.setData(Uri.parse("sms:" + aluno.getTelefone()));
        intentSms.putExtra("sms_body", "Mensagem");
        sms.setIntent(intentSms);

        adicionarMenuEncontrarNoMapa(menu);

        MenuItem site = menu.add("Navegar no site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String http = aluno.getSite().contains("http://") ? "" : "http://";
        intentSite.setData(Uri.parse(http + aluno.getSite()));
        site.setIntent(intentSite);

        MenuItem email = menu.add("Enviar E-mail");

        Intent intentEmail = new Intent(Intent.ACTION_SEND);
        intentEmail.setType("message/rtc822");
        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"palerique@gmail.com"});
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Testando subject do email");
        intentEmail.putExtra(Intent.EXTRA_TEXT, "Testando corpo do email");
        email.setIntent(intentEmail);


        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlunoDAO(ListaAlunos.this).delete(aluno);
                carregaLista();
                return false;
            }
        });

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    private void adicionarMenuEncontrarNoMapa(ContextMenu menu) {
        MenuItem acharNoMapa = menu.add("Achar no mapa");

        acharNoMapa.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String uriBegin = "geo:0,0";
                String encodedQuery = Uri.encode(aluno.getEndereco());
                String uriString = uriBegin + "?q=" + encodedQuery;
                Uri uri = Uri.parse(uriString);

                Intent intentMapa = new Intent(Intent.ACTION_VIEW);
                //      intentMapa.setData(Uri.parse("geo:0,0?q=" + retirarEspacos(aluno.getEndereco())));
                intentMapa.setData(uri);

                if (intentMapa.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentMapa);
                }

                return false;
            }
        });
    }

    private String retirarEspacos(String endereco) {
        return endereco.replace(" ", "+");
    }
}
