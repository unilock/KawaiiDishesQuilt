package com.hakimen.kawaiidishes.utils;

import com.hakimen.kawaiidishes.items.CatEars;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class KawaiiMessages {
    public static ArrayList<String> defaultMessages = new ArrayList<>();
    public static ArrayList<String> catEarsMessages = new ArrayList<>();
    public static ArrayList<String> maidCostumeMessages = new ArrayList<>();

    public static ArrayList<String> catEarsMaidCostumeMessages = new ArrayList<>();

    static {
        defaultMessages.add("You look so kawaii !!");
        defaultMessages.add("Cute <3 ");
        defaultMessages.add("You look nice today");
        defaultMessages.add("Hi Cutie !");

        catEarsMessages.add("OMG, CAT EARS");
        catEarsMessages.add("Wait... they are wearing cat ears !");
        catEarsMessages.add("Come back neko-chan");

        maidCostumeMessages.add("No way, a cute maid is here");


        catEarsMaidCostumeMessages.add("Omg a maid, if only they where with cat ea..., NO WAY THEY HAVE CAT EARS");
    }

    public static void sendMessage(LivingEntity entity, Player target){

        boolean hasCatEars,hasMaidDress = false;
        Random r = target.getRandom();
        hasCatEars = target.getInventory().getArmor(EquipmentSlot.HEAD.getIndex()).getItem() instanceof CatEars;

        var msg = new TextComponent("<").append(entity.getDisplayName()).append(">");
        int size;

        if(hasCatEars && hasMaidDress){
            size = catEarsMaidCostumeMessages.size();
            int option = r.nextInt(0,size);
            msg.append(catEarsMaidCostumeMessages.get(option));
        }else if(hasMaidDress){
            size = maidCostumeMessages.size();
            int option = r.nextInt(0,size);
            msg.append(maidCostumeMessages.get(option));
        }else if(hasCatEars){
            size = catEarsMessages.size();
            int option = r.nextInt(0,size);
            msg.append(catEarsMessages.get(option));
        }else{
            size = defaultMessages.size();
            int option = r.nextInt(0,size);
            msg.append(defaultMessages.get(option));
        }

        target.sendMessage(msg,entity.getUUID());
    }
}
