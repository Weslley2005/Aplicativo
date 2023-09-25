package br.unigran.aplicativo.persistence;

import java.util.List;

import br.unigran.aplicativo.model.Aplicativo;


public interface AplicativoDao {
    public void salvar(Aplicativo a);

    public void editar(Aplicativo a);

    public void remove(Aplicativo a);

    public List listagem();
}
