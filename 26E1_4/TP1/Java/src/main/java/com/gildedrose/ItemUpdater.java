package com.gildedrose;

/*
 * Contrato de atualização de itens.
 * Cada implementação decide se suporta um item e aplica sua regra específica.
 */
interface ItemUpdater {
    boolean supports(Item item);

    void update(Item item);
}
