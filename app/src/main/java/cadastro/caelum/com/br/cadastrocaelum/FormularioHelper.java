package cadastro.caelum.com.br.cadastrocaelum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import cadastro.caelum.com.br.cadastrocaelum.modelo.Aluno;

/**
 * Created by ph on 25/11/14.
 */
public class FormularioHelper {

    private final EditText campoNome;
    private final EditText campoSite;
    private final EditText campoEndereco;
    private final EditText campoTelefone;
    private final RatingBar ratingNota;
    private final ImageView foto;
    private Aluno aluno;

    public FormularioHelper(Formulario formulario) {

        campoNome = (EditText) formulario.findViewById(R.id.editText_nome);
        campoSite = (EditText) formulario.findViewById(R.id.editText_site);
        campoEndereco = (EditText) formulario.findViewById(R.id.editText_endereco);
        campoTelefone = (EditText) formulario.findViewById(R.id.editText_telefone);
        ratingNota = (RatingBar) formulario.findViewById(R.id.ratingBar_nota);
        foto = (ImageView) formulario.findViewById(R.id.imageViewFoto);
        aluno = new Aluno();

    }

    public Aluno extraiAlunoDoFormulario() {

        Aluno alunoExtraido = new Aluno();

        alunoExtraido.setNome(campoNome.getText().toString());
        alunoExtraido.setSite(campoSite.getText().toString());
        alunoExtraido.setEndereco(campoEndereco.getText().toString());
        alunoExtraido.setTelefone(campoTelefone.getText().toString());
        alunoExtraido.setNota(ratingNota.getRating());
        alunoExtraido.setFoto(aluno.getFoto());

        return alunoExtraido;
    }

    public void colocaAlunoNoFormulario(Aluno alunoRecebido) {

        aluno = alunoRecebido;

        campoNome.setText(alunoRecebido.getNome());
        campoSite.setText(alunoRecebido.getSite());
        campoEndereco.setText(alunoRecebido.getEndereco());
        campoTelefone.setText(alunoRecebido.getTelefone());
        ratingNota.setRating(alunoRecebido.getNota());

        if (aluno.getFoto() != null) {
            carregaImagem(aluno.getFoto());
        }
    }

    public ImageView getFoto() {
        return foto;
    }

    public void carregaImagem(String caminho) {
        aluno.setFoto(caminho);

        Bitmap imagem = BitmapFactory.decodeFile(caminho);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imagem, 100, 100, true);

        foto.setImageBitmap(scaledBitmap);
    }
}
