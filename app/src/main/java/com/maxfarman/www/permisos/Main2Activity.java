package com.maxfarman.www.permisos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.PACKAGE_USAGE_STATS;

public class Main2Activity extends AppCompatActivity {
    RelativeLayout l;
    Snackbar esnak;
    Button btn_ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn_ubicacion=findViewById(R.id.button2);
        btn_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this,MapsActivity.class));
            }
        });
        btn_ubicacion.setEnabled(false);
        l=findViewById(R.id.milayout);
        String mensaje="Faltan otorgar algunos permisos.";
        esnak = Snackbar.make(l,mensaje,Snackbar.LENGTH_INDEFINITE);
        esnak.setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(Main2Activity.this, new String[]{
                        CAMERA,
                        ACCESS_COARSE_LOCATION,
                        ACCESS_FINE_LOCATION
                }, 100);
            }
        });
        if(verificarPermisos()){
            iniciarApp();

        }
        else
        {
            solicitarPermisos();

        }

    }

    private void solicitarPermisos() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,CAMERA)||
                ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,ACCESS_FINE_LOCATION))
            {
                esnak.show();
            }
            else
        {
            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{
                    CAMERA,
                    ACCESS_COARSE_LOCATION,
                    ACCESS_FINE_LOCATION

                    ///podemos cambiar a cualquier componente
            }, 100);
        }
    }

    private boolean verificarPermisos() {
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.ICE_CREAM_SANDWICH)//es para las versiones de celulra android 4 ejemplo
        {
            return true;
        }
        if(ContextCompat.checkSelfPermission(Main2Activity.this,CAMERA)== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(Main2Activity.this,ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(Main2Activity.this,ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }

        return false;
    }

    private void iniciarApp() {
        btn_ubicacion.setEnabled(true);
        Context c=getApplication();
        String m="La aplicación ya tiene todos los permisos necsesarios para iniciar";
        Toast.makeText(c,m, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            if(grantResults[0]==getPackageManager().PERMISSION_GRANTED &&
                    grantResults[1]==getPackageManager().PERMISSION_GRANTED &&
                    grantResults[2]==getPackageManager().PERMISSION_GRANTED
                    )
            {
                    iniciarApp();
            }
            else
            {
                Context c= getApplication();
                String mensaje="No has otorgado todos los permisos";
                Toast.makeText(c,mensaje,Toast.LENGTH_LONG).show();
                esnak.show();
            }
        }

    }
}
