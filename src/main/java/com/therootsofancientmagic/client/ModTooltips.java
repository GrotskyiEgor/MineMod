package com.therootsofancientmagic.client;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;

public class ModTooltips {

    public static void register() {
        // ОСТАВЛЯЕМ ТОЛЬКО ОДИН УНИВЕРСАЛЬНЫЙ ИВЕНТ
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            
            // Получаем стандартный ключ перевода предмета или блока (например, "item.mod.fire_staff")
            String baseKey = stack.getItem().getTranslationKey();
            String descriptionKey = baseKey + ".description";

            // Проверяем, существует ли для него описание в файле en_us.json
            if (net.minecraft.client.resource.language.I18n.hasTranslation(descriptionKey)) {
                // Получаем сырую строку из файла локализации
                String fullDescription = net.minecraft.client.resource.language.I18n.translate(descriptionKey);
                
                // ЖЕСТКО РАЗДЕЛЯЕМ её по символу переноса строки \n
                String[] splitLines = fullDescription.split("\n");
                
                // Добавляем каждую строчку отдельно, что гарантирует работу переносов
                for (String line : splitLines) {
                    lines.add(Text.literal(line));
                }
            }
        });
    }
}
