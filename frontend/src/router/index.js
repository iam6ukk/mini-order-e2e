import CartPage from "@/components/CartPage.vue";
import OrderCompletePage from "@/components/OrderCompletePage.vue";
import OrderHistoryPage from "@/components/OrderHistoryPage.vue";
import ProductListPage from "@/components/ProductListPage.vue";
import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/products",
    },
    {
      path: "/products",
      component: ProductListPage,
    },
    {
      path: "/cart",
      component: CartPage,
    },
    {
      path: "/orders/complete",
      component: OrderCompletePage,
    },
    {
      path: "/orders",
      component: OrderHistoryPage,
    },
  ],
});

export default router;
