package com.gildedrose;

import java.util.List;

/*
 * Avaliação de design:
 * 1) A estrutura atual facilita a adição de novos tipos de item, pois `GildedRose` delega regras para implementações de
 *    `ItemUpdater`. Isso reduz alterações no método principal e aproxima o código do Princípio Aberto-Fechado.
 * 2) Cada `ItemUpdater` tem uma única responsabilidade: aplicar as regras de atualização de um tipo específico de item,
 *    atendendo ao Princípio da Responsabilidade Única.
 * 3) Não há violação de Liskov na hierarquia adotada, pois as implementações de `ItemUpdater` respeitam o mesmo contrato
 *    (`supports` e `update`) sem exigir pré-condições incompatíveis nem quebrar o comportamento esperado pelo chamador.
 */
class GildedRose {
    Item[] items;
    private final List<ItemUpdater> itemUpdaters;

    public GildedRose(Item[] items) {
        this.items = items;
        this.itemUpdaters = List.of(
                new AgedBrieUpdater(),
                new BackstagePassUpdater(),
                new SulfurasUpdater(),
                new ConjuredItemUpdater(),
                new DefaultUpdater()
        );
    }

    public void updateQuality() {
        for (Item item : items) {
            ItemUpdater updater = resolveUpdater(item);
            if (isAgedBrie(item)) {
                updateAgedBrie(item, updater);
            } else if (isBackstagePass(item)) {
                updateBackstagePass(item, updater);
            } else if (isSulfuras(item)) {
                updateSulfuras(item, updater);
            } else {
                updater.update(item);
            }
        }
    }

    private void updateAgedBrie(Item item, ItemUpdater updater) {
        updater.update(item);
    }

    private void updateBackstagePass(Item item, ItemUpdater updater) {
        updater.update(item);
    }

    private void updateSulfuras(Item item, ItemUpdater updater) {
        updater.update(item);
    }

    private ItemUpdater resolveUpdater(Item item) {
        return itemUpdaters.stream()
                .filter(updater -> updater.supports(item))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nenhum ItemUpdater encontrado para: " + item.name));
    }

    private boolean isAgedBrie(Item item) {
        return "Aged Brie".equals(item.name);
    }

    private boolean isBackstagePass(Item item) {
        return "Backstage passes to a TAFKAL80ETC concert".equals(item.name);
    }

    private boolean isSulfuras(Item item) {
        return "Sulfuras, Hand of Ragnaros".equals(item.name);
    }
}
