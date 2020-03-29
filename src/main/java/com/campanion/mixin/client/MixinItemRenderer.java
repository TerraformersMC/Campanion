package com.campanion.mixin.client;

import com.campanion.client.model.entity.SpearEntityModel;
import com.campanion.client.renderer.entity.SpearEntityRenderer;
import com.campanion.item.SpearItem;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.minecraft.client.render.item.ItemRenderer.getArmorVertexConsumer;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    private final Model spearEntityModel = new SpearEntityModel();

    @Shadow
    @Final
    private ItemModels models;


    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;II)V", at = @At("HEAD"), cancellable = true)
    public void renderItem(@Nullable LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, @Nullable World world, int light, int overlay, CallbackInfo info) {
        if(stack.getItem() instanceof SpearItem) {
            matrices.push();

            BakedModel model = this.models.getModel(stack);

            boolean renderNormal = renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND || renderMode == ModelTransformation.Mode.FIXED;
            String tridentLocation = "";
            if(!renderNormal) {
                tridentLocation = "_in_hand";
            }

            BakedModel transformationModel = this.models.getModelManager().getModel(new ModelIdentifier("minecraft:trident" + tridentLocation + "#inventory"));
            transformationModel.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            matrices.translate(-0.5D, -0.5D, -0.5D);

            if(renderNormal) {
                RenderLayer renderLayer = RenderLayers.getItemLayer(stack);
                RenderLayer renderLayer3;
                if (Objects.equals(renderLayer, TexturedRenderLayers.getEntityTranslucent())) {
                    renderLayer3 = TexturedRenderLayers.getEntityTranslucentCull();
                } else {
                    renderLayer3 = renderLayer;
                }

                VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, renderLayer3, true, stack.hasEnchantmentGlint());
                this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
            } else {
                matrices.push();

                if(entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && (renderMode == ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND || renderMode == ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND)) {
                    matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
                    matrices.translate(0, 1.5, 0);
                }

                matrices.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer spear = ItemRenderer.getArmorVertexConsumer(
                    vertexConsumers,
                    this.spearEntityModel.getLayer(SpearEntityRenderer.getTexture(((SpearItem)stack.getItem()).getType())),
                    false,
                    stack.hasEnchantmentGlint()
                );
                this.spearEntityModel.render(matrices, spear, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrices.pop();
            }

            matrices.pop();

            info.cancel();
        }
    }

    @Shadow
    private void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices) {
    }
}
