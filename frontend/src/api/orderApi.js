import apiClient from "./apiClient";

// 주문 내역 조회
export const getOrders = () => {
  return apiClient.get("/orders");
};

// 주문 생성
export const createOrder = () => {
  return apiClient.post("/orders");
};
