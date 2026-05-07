import { useState, useEffect } from 'react';
import { EavEntity, EntityType, Attribute, CreateEavEntityRequest, AttributeType } from '../types';
import { eavEntityApi, entityTypeApi, attributeApi } from '../api/eav';
import { Table } from '../components/Table';
import { Modal } from '../components/Modal';

export function EavEntityPage() {
  const [entities, setEntities] = useState<EavEntity[]>([]);
  const [entityTypes, setEntityTypes] = useState<EntityType[]>([]);
  const [attributes, setAttributes] = useState<Attribute[]>([]);
  const [selectedEntityTypeId, setSelectedEntityTypeId] = useState<number | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<EavEntity | null>(null);
  const [formData, setFormData] = useState<CreateEavEntityRequest>({
    code: '',
    name: '',
    attributes: {},
  });

  useEffect(() => {
    loadEntityTypes();
  }, []);

  useEffect(() => {
    if (selectedEntityTypeId) {
      loadEntities(selectedEntityTypeId);
      loadAttributes(selectedEntityTypeId);
    } else {
      setEntities([]);
      setAttributes([]);
    }
  }, [selectedEntityTypeId]);

  const loadEntityTypes = async () => {
    try {
      const data = await entityTypeApi.getAll();
      setEntityTypes(data);
      if (data.length > 0) {
        setSelectedEntityTypeId(data[0].id);
      }
    } catch (error) {
      console.error('Failed to load entity types:', error);
    }
  };

  const loadEntities = async (entityTypeId: number) => {
    try {
      const data = await eavEntityApi.getAll(entityTypeId);
      setEntities(data);
    } catch (error) {
      console.error('Failed to load entities:', error);
    }
  };

  const loadAttributes = async (entityTypeId: number) => {
    try {
      const data = await attributeApi.getAll(entityTypeId);
      setAttributes(data);
    } catch (error) {
      console.error('Failed to load attributes:', error);
    }
  };

  const handleOpenModal = (item?: EavEntity) => {
    if (item) {
      setEditingItem(item);
      setFormData({
        code: item.code,
        name: item.name,
        attributes: item.attributes || {},
      });
    } else {
      setEditingItem(null);
      const defaultAttrs: Record<string, unknown> = {};
      attributes.forEach((attr) => {
        if (attr.defaultValue) {
          defaultAttrs[attr.code] = parseAttributeValue(attr.defaultValue, attr.dataType);
        }
      });
      setFormData({ code: '', name: '', attributes: defaultAttrs });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingItem(null);
    setFormData({ code: '', name: '', attributes: {} });
  };

  const parseAttributeValue = (value: string, type: AttributeType): unknown => {
    switch (type) {
      case 'INTEGER':
        return parseInt(value, 10);
      case 'DECIMAL':
        return parseFloat(value);
      case 'BOOLEAN':
        return value.toLowerCase() === 'true';
      default:
        return value;
    }
  };

  const handleAttributeChange = (attrCode: string, value: unknown) => {
    setFormData({
      ...formData,
      attributes: {
        ...formData.attributes,
        [attrCode]: value,
      },
    });
  };

  const handleSubmit = async () => {
    if (!selectedEntityTypeId) return;
    try {
      if (editingItem) {
        await eavEntityApi.update(editingItem.id, formData);
      } else {
        await eavEntityApi.create(selectedEntityTypeId, formData);
      }
      handleCloseModal();
      loadEntities(selectedEntityTypeId);
    } catch (error) {
      console.error('Failed to save entity:', error);
    }
  };

  const handleDelete = async (item: EavEntity) => {
    if (confirm(`确定删除实体 "${item.name}" 吗？`)) {
      try {
        await eavEntityApi.delete(item.id);
        if (selectedEntityTypeId) {
          loadEntities(selectedEntityTypeId);
        }
      } catch (error) {
        console.error('Failed to delete entity:', error);
      }
    }
  };

  const renderAttributeValue = (entity: EavEntity, attrCode: string) => {
    const value = entity.attributes?.[attrCode];
    if (value === undefined || value === null) return '-';
    return String(value);
  };

  const columns = [
    { key: 'code', label: '编码' },
    { key: 'name', label: '名称' },
    ...attributes.map((attr) => ({
      key: attr.code,
      label: attr.name,
      render: (entity: EavEntity) => renderAttributeValue(entity, attr.code),
    })),
    { key: 'createdAt', label: '创建时间', render: (item) => new Date(item.createdAt).toLocaleString() },
  ];

  const renderAttributeInput = (attr: Attribute) => {
    const value = formData.attributes?.[attr.code];
    switch (attr.dataType) {
      case 'BOOLEAN':
        return (
          <input
            type="checkbox"
            checked={value === true}
            onChange={(e) => handleAttributeChange(attr.code, e.target.checked)}
            style={styles.checkbox}
          />
        );
      case 'INTEGER':
        return (
          <input
            type="number"
            value={value || ''}
            onChange={(e) => handleAttributeChange(attr.code, e.target.value ? Number(e.target.value) : undefined)}
            style={styles.input}
          />
        );
      case 'DECIMAL':
        return (
          <input
            type="number"
            step="any"
            value={value || ''}
            onChange={(e) => handleAttributeChange(attr.code, e.target.value ? parseFloat(e.target.value) : undefined)}
            style={styles.input}
          />
        );
      case 'DATE':
        return (
          <input
            type="date"
            value={typeof value === 'string' ? value : ''}
            onChange={(e) => handleAttributeChange(attr.code, e.target.value || undefined)}
            style={styles.input}
          />
        );
      case 'DATETIME':
        return (
          <input
            type="datetime-local"
            value={typeof value === 'string' ? value.replace(' ', 'T') : ''}
            onChange={(e) => handleAttributeChange(attr.code, e.target.value ? e.target.value.replace('T', ' ') : undefined)}
            style={styles.input}
          />
        );
      default:
        return (
          <input
            type="text"
            value={value || ''}
            onChange={(e) => handleAttributeChange(attr.code, e.target.value || undefined)}
            style={styles.input}
            maxLength={attr.maxLength}
          />
        );
    }
  };

  return (
    <div>
      <div style={styles.header}>
        <h2>实体管理</h2>
        <select
          value={selectedEntityTypeId || ''}
          onChange={(e) => setSelectedEntityTypeId(Number(e.target.value))}
          style={styles.select}
        >
          <option value="">选择实体类型</option>
          {entityTypes.map((et) => (
            <option key={et.id} value={et.id}>{et.name}</option>
          ))}
        </select>
        {selectedEntityTypeId && (
          <button onClick={() => handleOpenModal()} style={styles.btnAdd}>
            + 新增
          </button>
        )}
      </div>
      <Table
        data={entities}
        columns={columns}
        onEdit={handleOpenModal}
        onDelete={handleDelete}
      />

      <Modal isOpen={isModalOpen} onClose={handleCloseModal} title={editingItem ? '编辑实体' : '新增实体'}>
        <div style={styles.form}>
          <div style={styles.formGroup}>
            <label style={styles.label}>编码 *</label>
            <input
              type="text"
              value={formData.code}
              onChange={(e) => setFormData({ ...formData, code: e.target.value })}
              style={styles.input}
              required
            />
          </div>
          <div style={styles.formGroup}>
            <label style={styles.label}>名称 *</label>
            <input
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              style={styles.input}
              required
            />
          </div>
          <div style={styles.divider}>
            <h3>属性值</h3>
          </div>
          {attributes.map((attr) => (
            <div key={attr.id} style={styles.formGroup}>
              <label style={styles.label}>
                {attr.name}
                {attr.required && <span style={styles.required}> *</span>}
              </label>
              {renderAttributeInput(attr)}
            </div>
          ))}
          {attributes.length === 0 && (
            <div style={styles.noAttrs}>该实体类型暂无属性，请先添加属性</div>
          )}
          <div style={styles.formActions}>
            <button onClick={handleCloseModal} style={styles.btnCancel}>
              取消
            </button>
            <button onClick={handleSubmit} style={styles.btnSubmit}>
              {editingItem ? '保存' : '创建'}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
}

const styles = {
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: '1rem',
    gap: '1rem',
  },
  select: {
    padding: '8px 12px',
    border: '1px solid #ced4da',
    borderRadius: '4px',
    fontSize: '1rem',
    minWidth: '200px',
  },
  btnAdd: {
    padding: '8px 16px',
    backgroundColor: '#28a745',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontSize: '1rem',
    ':hover': {
      backgroundColor: '#218838',
    },
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '1rem',
  },
  formGroup: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.25rem',
  },
  label: {
    fontWeight: 500,
    color: '#495057',
  },
  required: {
    color: '#dc3545',
  },
  input: {
    padding: '8px 12px',
    border: '1px solid #ced4da',
    borderRadius: '4px',
    fontSize: '1rem',
    ':focus': {
      outline: 'none',
      borderColor: '#80bdff',
      boxShadow: '0 0 0 3px rgba(0,123,255,0.25)',
    },
  },
  checkbox: {
    width: '18px',
    height: '18px',
    cursor: 'pointer',
  },
  divider: {
    paddingTop: '1rem',
    borderTop: '1px solid #dee2e6',
    h3: {
      margin: '0 0 0.5rem 0',
      fontSize: '1rem',
      fontWeight: 600,
    },
  },
  noAttrs: {
    color: '#6c757d',
    fontStyle: 'italic',
    padding: '0.5rem',
  },
  formActions: {
    display: 'flex',
    justifyContent: 'flex-end',
    gap: '0.5rem',
    marginTop: '1rem',
  },
  btnCancel: {
    padding: '8px 16px',
    backgroundColor: '#6c757d',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontSize: '1rem',
    ':hover': {
      backgroundColor: '#5a6268',
    },
  },
  btnSubmit: {
    padding: '8px 16px',
    backgroundColor: '#007bff',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontSize: '1rem',
    ':hover': {
      backgroundColor: '#0069d9',
    },
  },
};
