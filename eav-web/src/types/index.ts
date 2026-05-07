export type AttributeType = 'STRING' | 'INTEGER' | 'DECIMAL' | 'BOOLEAN' | 'DATE' | 'DATETIME';

export interface EntityType {
  id: number;
  code: string;
  name: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface Attribute {
  id: number;
  entityTypeId: number;
  code: string;
  name: string;
  description: string;
  dataType: AttributeType;
  required: boolean;
  defaultValue: string | null;
  minValue: number | null;
  maxValue: number | null;
  maxLength: number | null;
  sortOrder: number;
  createdAt: string;
  updatedAt: string;
}

export interface EavEntity {
  id: number;
  entityTypeId: number;
  code: string;
  name: string;
  attributes: Record<string, unknown>;
  createdAt: string;
  updatedAt: string;
}

export interface CreateEntityTypeRequest {
  code: string;
  name: string;
  description?: string;
}

export interface CreateAttributeRequest {
  code: string;
  name: string;
  description?: string;
  dataType: AttributeType;
  required?: boolean;
  defaultValue?: string;
  minValue?: number;
  maxValue?: number;
  maxLength?: number;
  sortOrder?: number;
}

export interface CreateEavEntityRequest {
  code: string;
  name: string;
  attributes?: Record<string, unknown>;
}
