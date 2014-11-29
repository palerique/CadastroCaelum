package cadastro.caelum.com.br.cadastrocaelum.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cadastro.caelum.com.br.cadastrocaelum.R;
import cadastro.caelum.com.br.cadastrocaelum.modelo.Aluno;

/**
 * Created by ph on 29/11/14.
 */
public class ListaAlunosAdapter extends BaseAdapter {

    private List<Aluno> alunos;
    private Activity activity;

    public ListaAlunosAdapter(List<Aluno> alunos, Activity activity) {
        this.alunos = alunos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Aluno aluno = alunos.get(position);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.linha_listagem, null);

        TextView nomeDoAlunoTV = (TextView) view.findViewById(R.id.nomeDoAlunoNaLista);
        nomeDoAlunoTV.setText(aluno.getNome());

        ImageView fotoDoAlunoIV = (ImageView) view.findViewById(R.id.fotoDoAlunoNaLista);
        if (aluno.getFoto() != null) {
            Bitmap fotoDoAluno = BitmapFactory.decodeFile(aluno.getFoto());
            Bitmap fotoDoAlunoReduzida = Bitmap.createScaledBitmap(fotoDoAluno, 100, 100, true);

            fotoDoAlunoIV.setImageBitmap(fotoDoAlunoReduzida);
        } else {
            Bitmap semFoto = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_no_image);
            Bitmap fotoDoAlunoReduzida = Bitmap.createScaledBitmap(semFoto, 100, 100, true);

            fotoDoAlunoIV.setImageBitmap(fotoDoAlunoReduzida);
        }

        if (position % 2 == 0) {
            view.setBackgroundColor(activity.getResources().
                    getColor(R.color.linha_par));
        }

        return view;
    }
}
