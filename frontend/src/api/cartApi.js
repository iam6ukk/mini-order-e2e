import apiClient from "./apiClient";

// 장바구니 아이템 조회
export const getCartItems = () => {
  return apiClient.get("/cart/items");
};

// 장바구니 아이템 추가
export const addCartItem = (productId, quantity) => {
  return apiClient.post("/cart/items", { productId, quantity });
};

// 장바구니 아이템 개별 삭제
export const deleteCartItem = (cartItemId) => {
  return apiClient.delete(`/cart/items/${cartItemId}`);
};

// 장바구니 아이템 전체 삭제
export const clearCartItems = () => {
  return apiClient.delete("/cart/items");
};
