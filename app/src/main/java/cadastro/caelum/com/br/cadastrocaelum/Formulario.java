package cadastro.caelum.com.br.cadastrocaelum;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import cadastro.caelum.com.br.cadastrocaelum.dao.AlunoDAO;
import cadastro.caelum.com.br.cadastrocaelum.modelo.Aluno;

/**
 * Created by ph on 23/11/14.
 */
public class Formulario extends Activity {

    private FormularioHelper helper;
    private String caminho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario);

        Intent intent = getIntent();

        final Aluno alunoSelecionado;

        Bundle extras = intent.getExtras();

        if (extras != null) {
            alunoSelecionado = (Aluno) extras.get(Extras.ALUNO_SELECIONADO);
        } else {
            alunoSelecionado = null;
        }

        Button button = (Button) findViewById(R.id.button_gravar);
        helper = new FormularioHelper(this);

        if (alunoSelecionado != null) {

            button.setText("Alterar");
            helper.colocaAlunoNoFormulario(alunoSelecionado);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aluno aluno = helper.extraiAlunoDoFormulario();

                AlunoDAO dao = new AlunoDAO(Formulario.this);

                if (alunoSelecionado == null) {
                    dao.save(aluno);
                } else {
                    aluno.setId(alunoSelecionado.getId());
                    dao.save(aluno);
                }

                dao.close();

                Toast.makeText(Formulario.this, "Nome do Aluno: " + aluno.getNome(), Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        ImageView foto = helper.getFoto();

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaACamero = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                caminho = Environment.getExternalStorageDirectory().toString() + "/" + System.currentTimeMillis() + ".jpg";

                Uri uri = Uri.fromFile(new File(caminho));

                irParaACamero.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                startActivityForResult(irParaACamero, Extras.REQUEST_CODE_CAMERA_RESULT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Extras.REQUEST_CODE_CAMERA_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                helper.carregaImagem(caminho);
            } else {
                caminho = null;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
