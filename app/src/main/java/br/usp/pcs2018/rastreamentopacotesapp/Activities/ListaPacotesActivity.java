package br.usp.pcs2018.rastreamentopacotesapp.Activities;

import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import br.usp.pcs2018.rastreamentopacotesapp.Adapters.TabLayoutAdapter;
import br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks.HttpRequestTask;
import br.usp.pcs2018.rastreamentopacotesapp.Fragments.ListaPacotesFragment;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpResponse;
import br.usp.pcs2018.rastreamentopacotesapp.R;

import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_HTTP_CODE;
import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_TIMER_CODE;

public class ListaPacotesActivity extends _BaseActivity {

    private TabLayout tabs;
    private ViewPager container;

    private ListaPacotesFragment FragmentAtivo;
    private ListaPacotesFragment FragmentHistorico;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        setContentView(R.layout.activity_lista_pacotes);

        tabs = findViewById(R.id.tabs_holder);
        container = findViewById(R.id.container);

        TabLayoutAdapter adapter = new TabLayoutAdapter(getSupportFragmentManager());

        FragmentAtivo = ListaPacotesFragment.newInstance("ativo");
        FragmentHistorico = ListaPacotesFragment.newInstance("historico");

        adapter.addFragment(FragmentAtivo);
        adapter.addFragment(FragmentHistorico);


        container.setOffscreenPageLimit(2);
        container.setAdapter(adapter);
        tabs.setupWithViewPager(container);

        tabs.getTabAt(0).setText(getString(R.string.pacote_transporte));
        tabs.getTabAt(1).setText(getString(R.string.pacote_historico));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                container.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public void onAsyncFinished(Object obj, int callerCode, int type) {
        switch (type) {
            case ASYNC_HTTP_CODE:
                switch (callerCode){
                    case HttpRequestTask.PACOTES_ATIVOS:
                        FragmentAtivo.listarPacotes((HttpResponse) obj);
                        break;
                    case HttpRequestTask.PACOTES_HISTORICO:
                        FragmentHistorico.listarPacotes((HttpResponse) obj);
                        break;
                    default:
                        break;
                }
                break;
            case ASYNC_TIMER_CODE:
                break;
            default:
                break;
        }
    }
}
