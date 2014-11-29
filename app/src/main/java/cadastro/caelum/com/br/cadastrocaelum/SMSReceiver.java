package cadastro.caelum.com.br.cadastrocaelum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.widget.Toast;

import cadastro.caelum.com.br.cadastrocaelum.dao.AlunoDAO;

/**
 * Created by ph on 29/11/14.
 */
public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] mensagens = (Object[]) intent.getExtras().get("pdus");

        byte[] primeira = (byte[]) mensagens[0];

        SmsMessage sms = SmsMessage.createFromPdu(primeira);

        String address = sms.getDisplayOriginatingAddress();

        Toast.makeText(context, "SMS do telefone " + address, Toast.LENGTH_LONG).show();

        AlunoDAO dao = new AlunoDAO(context);

        boolean isAluno = dao.isAluno(address);

        if (isAluno) {
            MediaPlayer player = MediaPlayer.create(context, R.raw.msg);
            player.start();
            Toast.makeText(context, "Tocando musica... ", Toast.LENGTH_SHORT).show();
        }

        dao.close();
    }
}
