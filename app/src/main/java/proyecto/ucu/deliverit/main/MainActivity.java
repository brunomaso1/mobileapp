package proyecto.ucu.deliverit.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import proyecto.ucu.deliverit.R;
import proyecto.ucu.deliverit.almacenamiento.DataBase;
import proyecto.ucu.deliverit.almacenamiento.SharedPref;
import proyecto.ucu.deliverit.custom_adapters.CustomAdapterForSidebar;
import proyecto.ucu.deliverit.custom_adapters.CustomAdapterForViajesPublicados;
import proyecto.ucu.deliverit.custom_adapters.Sidebar;
import proyecto.ucu.deliverit.entidades.Pedido;
import proyecto.ucu.deliverit.entidades.Viaje;
import proyecto.ucu.deliverit.tasks.AceptarViajeTask;
import proyecto.ucu.deliverit.tasks.ActualizarTokenTask;
import proyecto.ucu.deliverit.tasks.SolicitarPedidosTask;
import proyecto.ucu.deliverit.tasks.ViajesPublicadosTask;
import proyecto.ucu.deliverit.utiles.Valores;

public class MainActivity extends AppCompatActivity {
    private List<Viaje> viajes;
    private Viaje viaje;

    private String[] opciones_sidebar_array;
    private int[] iconos_array;

    private ListView viajes_lv, sidebar_lv;
    private ImageButton mapa_ibtn;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle mDrawerToggle;

    public CustomAdapterForSidebar sidebarAdapter;
    public CustomAdapterForViajesPublicados adapter;

    private DataBase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        DB = new DataBase(MainActivity.this);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viajes_lv = (ListView) findViewById(R.id.viajes_lv);
        mapa_ibtn = (ImageButton) findViewById(R.id.mapa_ibtn);
        sidebar_lv = (ListView) findViewById(R.id.sidebar_lv);

        crearSidebar();

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.sidebar_open, R.string.sidebar_close) {
            public void onDrawerClosed(View view) {}

            public void onDrawerOpened(View drawerView) {}
        };
        drawer_layout.setDrawerListener(mDrawerToggle);

        LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver,
                new IntentFilter("tokenReceiver"));
    }

    BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra("token");
            if(token != null) {
                SharedPref.guardarToken(MainActivity.this, token);
                System.out.println("token = " + token);
                new ActualizarTokenTask(MainActivity.this, token, (int) SharedPref.getIdDelivery(MainActivity.this)).execute();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        new ViajesPublicadosTask(MainActivity.this).execute();
    }



    public void viajesPublicadosTaskRetorno (List<Viaje> viajesPublicados) {
        if (viajesPublicados == null) {
            Toast.makeText(MainActivity.this, R.string.no_se_pudo_obtener_viajes, Toast.LENGTH_LONG).show();
        } else {
            if (viajesPublicados.size() == 0) {
                Toast.makeText(MainActivity.this, R.string.no_hay_viajes, Toast.LENGTH_LONG).show();
            } else {
                try {
                    viajes = eliminarRechazados(viajesPublicados);
                } catch (SQLiteException e) {
                    viajes = viajesPublicados;
                    Toast.makeText(MainActivity.this, R.string.no_se_pudieron_obtener_datos_base, Toast.LENGTH_LONG).show();
                }
                adapter = new CustomAdapterForViajesPublicados(viajes, MainActivity.this);
                viajes_lv.setAdapter(adapter);

                viajes_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        viaje = viajes.get(position);
                        new SolicitarPedidosTask(MainActivity.this, viaje.getId()).execute();
                    }
                });

                mapa_ibtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viajes.size() > 0) {
                            Intent intent = new Intent(MainActivity.this, SucursalesMapActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.no_existen_viajes_disponibles, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    public void actualizarTokenTaskRetorno() {}

    public void solicitarPedidosTaskRetorno(List<Pedido> pedidos) {
        try {
            DB.guardarDatosPedidos(pedidos);
        } catch (SQLiteException e) {
            Toast.makeText(MainActivity.this, R.string.no_se_pudo_insertar_en_la_base, Toast.LENGTH_LONG).show();
        }

        Intent i = new Intent(getApplicationContext(), NotificacionActivity.class);
        i.putExtra(Valores.NOTIFICATOIN_ID_TEXTO, Valores.NOTIFICATION_ID);
        i.putExtra(Valores.VIAJE, viaje.getId());
        startActivity(i);
    }

    private void crearSidebar () {
        iconos_array = getIconos();
        opciones_sidebar_array = getResources().getStringArray(R.array.opciones_sidebar_array);

        List<Sidebar> sidebar = new ArrayList<>();

        for (int i = 0; i < opciones_sidebar_array.length; i++) {
            Sidebar s = new Sidebar(iconos_array[i], opciones_sidebar_array[i]);
            sidebar.add(s);
        }

        sidebarAdapter = new CustomAdapterForSidebar(sidebar, MainActivity.this);
        sidebar_lv.setAdapter(sidebarAdapter);
        sidebar_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0: // Ingresos
                        intent = new Intent(MainActivity.this, IngresosActivity.class);
                        startActivity(intent);
                        break;
                    case 1: // Viajes aceptados
                        intent = new Intent(MainActivity.this, ViajesActivity.class);
                        intent.putExtra(Valores.ACTIVITY_PADRE, Valores.ACTIVITY_PADRE_MAIN);
                        startActivity(intent);
                        break;
                    case 4: // Salir
                        SharedPref.logout(MainActivity.this);
                        finish();
                        break;
                }
            }
        });
    }

    private int[] getIconos() {
        int[] iconos = new int[5];

        for (int i = 1; i <= 5; i++) {
            try {
                Class res = R.drawable.class;
                Field field = res.getField("ic_sidebar_" + i);
                iconos[i - 1] = field.getInt(null);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return iconos;
    }

    private List<Viaje> eliminarRechazados(List<Viaje> publicados) throws SQLiteException {
        List<Viaje> retorno = new ArrayList<>();

        List<Integer> rechazados = DB.getViajesRechazados();
        for (Viaje v : publicados) {
            if (!rechazados.contains(v.getId())) {
                retorno.add(v);
            }
        }

        return retorno;
    }
}