package br.unigran.aplicativo.persistence;

import java.util.ArrayList;
import java.util.List;

import br.unigran.aplicativo.model.Aplicativo;


public class AplicativoImpl implements AplicativoDao {

    private List lista;

    public AplicativoImpl() {
        lista = new ArrayList();
    }

    @Override
    public void salvar(Aplicativo a) {
        lista.add(a);
    }

    @Override
    public void editar(Aplicativo a) {
        if (lista.contains(a)) {
            lista.add(lista.indexOf(a), a);
        }
    }

    @Override
    public void remove(Aplicativo a) {
        lista.remove(a);
    }

    @Override
    public List listagem() {
        return lista;
    }
}
