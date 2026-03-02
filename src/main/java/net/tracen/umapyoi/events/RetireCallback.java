package net.tracen.umapyoi.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.ItemStack;
import net.tracen.umapyoi.item.ItemRegistry;
import net.tracen.umapyoi.registry.factors.UmaFactorStack;
import net.tracen.umapyoi.utils.UmaFactorUtils;
import net.tracen.umapyoi.utils.UmaSoulUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public interface RetireCallback {
    interface Pre {
        class Context {
            public final int randomSeed;
            private final List<UmaFactorStack> originalFactors;
            private List<UmaFactorStack> resultFactors;
            private final ItemStack soul;

            private ItemStack outputStack;

            public Context(int seed, List<UmaFactorStack> factors, ItemStack soul) {
                this.randomSeed = seed;
                this.originalFactors = Collections.unmodifiableList(factors);
                this.resultFactors = new ArrayList<>(factors);
                this.soul = soul;
            }

            public List<UmaFactorStack> getOutputFactors() {
                return Objects.requireNonNullElse(resultFactors, originalFactors);
            }

            public void setOutputFactors(List<UmaFactorStack> factors) {
                this.resultFactors = factors;
            }

            public void setOutputStack(ItemStack outputStack) {
                this.outputStack = outputStack;
            }

            public ItemStack getOutputStack() {
                return Objects.requireNonNullElseGet(outputStack, this::getDefaultOutputStack);
            }

            public static ItemStack getDefaultOutputStack(ItemStack soul, List<UmaFactorStack> factors) {
                ItemStack result = ItemRegistry.UMA_FACTOR_ITEM.get().getDefaultInstance();
                result.getOrCreateTag().putString("name", UmaSoulUtils.getName(soul).toString());
                result.getOrCreateTag().put("factors", UmaFactorUtils.serializeNBT(factors));
                return result;
            }

            public ItemStack getDefaultOutputStack() {
                return getDefaultOutputStack(this.soul, this.getOutputFactors());
            }

            public ItemStack getSoul() {
                return this.soul;
            }
        }

        /**
         * @return true to cancel the event, false to continue
         */
        boolean callback(Context context);

        Event<Pre> EVENT = EventFactory.createArrayBacked(Pre.class,
                (listeners) -> (context) -> {
                    for (Pre listener : listeners) {
                        if (listener.callback(context))
                            return true;
                    }

                    return false;
                });

        /**
         * @return true if the event was cancelled
         */
        static boolean invoke(Context context) {
            return EVENT.invoker().callback(context);
        }
    }

    interface Post {
        class Context {
            private final ItemStack stackSoulPre;
            private final ItemStack stackSoulPostDefault;
            private ItemStack stackSoulPost;
            private final ItemStack outputFactor;

            public Context(ItemStack pre, ItemStack post, ItemStack output) {
                this.stackSoulPre = pre;
                this.stackSoulPostDefault = post;
                this.outputFactor = output;
            }

            public ItemStack getStackSoulPre() {
                return this.stackSoulPre;
            }

            public ItemStack getOutputFactor() {
                return this.outputFactor;
            }

            public ItemStack getStackSoulPost() {
                return Objects.requireNonNullElse(this.stackSoulPost, this.stackSoulPostDefault);
            }

            public ItemStack getStackSoulPostDefault() {
                return this.stackSoulPostDefault;
            }

            public void setStackSoulPost(ItemStack stack) {
                this.stackSoulPost = stack;
            }
        }

        void callback(Context context);

        Event<Post> EVENT = EventFactory.createArrayBacked(Post.class,
                (listeners) -> (context) -> {
                    for (Post listener : listeners) {
                        listener.callback(context);
                    }
                });

        static void invoke(Context context) {
            EVENT.invoker().callback(context);
        }
    }
}
