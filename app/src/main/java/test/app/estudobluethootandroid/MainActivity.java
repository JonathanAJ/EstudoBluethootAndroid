package test.app.estudobluethootandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter meuBluetoothAdpt;
    private Set<BluetoothDevice> pareados;

    private TextView txtInfo;

    private ListView listaPareados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meuBluetoothAdpt = BluetoothAdapter.getDefaultAdapter();

        txtInfo = (TextView) findViewById(R.id.txtInfo);
        listaPareados = (ListView) findViewById(R.id.listPareados);
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(meuBluetoothAdpt != null){
            // se tiver desabilitado, conecte-se
            txtInfo.setText("Há suporte Bluetooth para seu dispositivo");
            if(!meuBluetoothAdpt.isEnabled()){
                Intent bluetoothConect = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(bluetoothConect, 0);
            }else{
                list();
                txtInfo.setText("Bluetooth conectado");
            }
        }else{
            txtInfo.setText("Não há suporte Bluetooth para seu dispositivo");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            txtInfo.setText("Bluetooth conectado");
            list();
        }else if(resultCode == RESULT_CANCELED){
            txtInfo.setText("Ação cancelada pelo usuário");
        }
    }

    public void tornarVisivel(View v){
        Intent visibilidade = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(visibilidade, 0);
    }

    public void list(){
        pareados = meuBluetoothAdpt.getBondedDevices();

        ArrayList<String> list = new ArrayList<>();
        Iterator<BluetoothDevice> it = pareados.iterator();
        while(it.hasNext()){
            BluetoothDevice dispositivo = it.next();
            list.add(dispositivo.getName() + " - " + dispositivo.getAddress());
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listaPareados.setAdapter(adapter);
    }
}
