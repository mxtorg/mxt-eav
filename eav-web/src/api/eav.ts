import axios from 'axios';
import {
  EntityType,
  Attribute,
  EavEntity,
  CreateEntityTypeRequest,
  CreateAttributeRequest,
  CreateEavEntityRequest,
} from '../types';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const entityTypeApi = {
  getAll: async (): Promise<EntityType[]> => {
    const response = await api.get('/entity-types');
    return response.data;
  },

  getById: async (id: number): Promise<EntityType> => {
    const response = await api.get(`/entity-types/${id}`);
    return response.data;
  },

  create: async (data: CreateEntityTypeRequest): Promise<EntityType> => {
    const response = await api.post('/entity-types', data);
    return response.data;
  },

  update: async (id: number, data: CreateEntityTypeRequest): Promise<EntityType> => {
    const response = await api.put(`/entity-types/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/entity-types/${id}`);
  },
};

export const attributeApi = {
  getAll: async (entityTypeId: number): Promise<Attribute[]> => {
    const response = await api.get(`/entity-types/${entityTypeId}/attributes`);
    return response.data;
  },

  getById: async (id: number): Promise<Attribute> => {
    const response = await api.get(`/attributes/${id}`);
    return response.data;
  },

  create: async (entityTypeId: number, data: CreateAttributeRequest): Promise<Attribute> => {
    const response = await api.post(`/entity-types/${entityTypeId}/attributes`, data);
    return response.data;
  },

  update: async (id: number, data: CreateAttributeRequest): Promise<Attribute> => {
    const response = await api.put(`/attributes/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/attributes/${id}`);
  },
};

export const eavEntityApi = {
  getAll: async (entityTypeId: number): Promise<EavEntity[]> => {
    const response = await api.get(`/entity-types/${entityTypeId}/entities`);
    return response.data;
  },

  getById: async (id: number): Promise<EavEntity> => {
    const response = await api.get(`/entities/${id}`);
    return response.data;
  },

  create: async (entityTypeId: number, data: CreateEavEntityRequest): Promise<EavEntity> => {
    const response = await api.post(`/entity-types/${entityTypeId}/entities`, data);
    return response.data;
  },

  update: async (id: number, data: CreateEavEntityRequest): Promise<EavEntity> => {
    const response = await api.put(`/entities/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/entities/${id}`);
  },
};
