import { useState, useEffect } from 'react';
import { Attribute, AttributeType, CreateAttributeRequest, EntityType } from '../types';
import { attributeApi, entityTypeApi } from '../api/eav';
import { Table } from '../components/Table';
import { Modal } from '../components/Modal';

const attributeTypes: { value: AttributeType; label: string }[] = [
  { value: 'STRING', label: '字符串' },
  { value: 'INTEGER', label: '整数' },
  { value: 'DECIMAL', label: '小数' },
  { value: 'BOOLEAN', label: '布尔' },
  { value: 'DATE', label: '日期' },
  { value: 'DATETIME', label: '日期时间' },
];

export function AttributePage() {
  const [attributes, setAttributes] = useState<Attribute[]>([]);
  const [entityTypes, setEntityTypes] = useState<EntityType[]>([]);
  const [selectedEntityTypeId, setSelectedEntityTypeId] = useState<number | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<Attribute | null>(null);
  const [formData, setFormData] = useState<CreateAttributeRequest>({
    code: '',
    name: '',
    description: '',
    dataType: 'STRING',
    required: false,
  });

  useEffect(() => {
    loadEntityTypes();
  }, []);

  useEffect(() => {
    if (selectedEntityTypeId) {
      loadAttributes(selectedEntityTypeId);
    } else {
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

  const loadAttributes = async (entityTypeId: number) => {
    try {
      const data = await attributeApi.getAll(entityTypeId);
      setAttributes(data);
    } catch (error) {
      console.error('Failed to load attributes:', error);
    }
  };

  const handleOpenModal = (item?: Attribute) => {
    if (item) {
      setEditingItem(item);
      setFormData({
        code: item.code,
        name: item.name,
        description: item.description || '',
        dataType: item.dataType,
        required: item.required,
        defaultValue: item.defaultValue || undefined,
        minValue: item.minValue || undefined,
        maxValue: item.maxValue || undefined,
        maxLength: item.maxLength || undefined,
        sortOrder: item.sortOrder,
      });
    } else {
      setEditingItem(null);
      setFormData({
        code: '',
        name: '',
        description: '',
        dataType: 'STRING',
        required: false,
        sortOrder: 0,
      });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingItem(null);
    setFormData({
      code: '',
      name: '',
      description: '',
      dataType: 'STRING',
      required: false,
      sortOrder: 0,
    });
  };

  const handleSubmit = async () => {
    if (!selectedEntityTypeId) return;
    try {
      if (editingItem) {
        await attributeApi.update(editingItem.id, formData);
      } else {
        await attributeApi.create(selectedEntityTypeId, formData);
      }
      handleCloseModal();
      loadAttributes(selectedEntityTypeId);
    } catch (error) {
      console.error('Failed to save attribute:', error);
    }
  };

  const handleDelete = async (item: Attribute) => {
    if (confirm(`确定删除属性 "${item.name}" 吗？`)) {
      try {
        await attributeApi.delete(item.id);
        if (selectedEntityTypeId) {
          loadAttributes(selectedEntityTypeId);
        }
      } catch (error) {
        console.error('Failed to delete attribute:', error);
      }
    }
  };

  const columns = [
    { key: 'code', label: '编码' },
    { key: 'name', label: '名称' },
    { key: 'dataType', label: '类型', render: (item) => attributeTypes.find(t => t.value === item.dataType)?.label || item.dataType },
    { key: 'required', label: '必填', render: (item) => item.required ? '是' : '否' },
    { key: 'sortOrder', label: '排序' },
  ];

  return (
    <div>
      <div style={styles.header}>
        <h2>属性管理</h2>
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
        data={attributes}
        columns={columns}
        onEdit={handleOpenModal}
        onDelete={handleDelete}
      />

      <Modal isOpen={isModalOpen} onClose={handleCloseModal} title={editingItem ? '编辑属性' : '新增属性'}>
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
          <div style={styles.formGroup}>
            <label style={styles.label}>类型 *</label>
            <select
              value={formData.dataType}
              onChange={(e) => setFormData({ ...formData, dataType: e.target.value as AttributeType })}
              style={styles.input}
            >
              {attributeTypes.map((type) => (
                <option key={type.value} value={type.value}>{type.label}</option>
              ))}
            </select>
          </div>
          <div style={styles.formGroup}>
            <label style={styles.label}>描述</label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              style={styles.textarea}
            />
          </div>
          <div style={styles.formGroup}>
            <label style={styles.label}>
              <input
                type="checkbox"
                checked={formData.required}
                onChange={(e) => setFormData({ ...formData, required: e.target.checked })}
              />
              必填
            </label>
          </div>
          <div style={styles.formGroup}>
            <label style={styles.label}>默认值</label>
            <input
              type="text"
              value={formData.defaultValue || ''}
              onChange={(e) => setFormData({ ...formData, defaultValue: e.target.value })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label style={styles.label}>最小值</label>
            <input
              type="number"
              value={formData.minValue || ''}
              onChange={(e) => setFormData({ ...formData, minValue: e.target.value ? Number(e.target.value) : undefined })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label style={styles.label}>最大值</label>
            <input
              type="number"
              value={formData.maxValue || ''}
              onChange={(e) => setFormData({ ...formData, maxValue: e.target.value ? Number(e.target.value) : undefined })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label style={styles.label}>最大长度</label>
            <input
              type="number"
              value={formData.maxLength || ''}
              onChange={(e) => setFormData({ ...formData, maxLength: e.target.value ? Number(e.target.value) : undefined })}
              style={styles.input}
            />
          </div>
          <div style={styles.formGroup}>
            <label style={styles.label}>排序</label>
            <input
              type="number"
              value={formData.sortOrder || 0}
              onChange={(e) => setFormData({ ...formData, sortOrder: Number(e.target.value) })}
              style={styles.input}
            />
          </div>
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
  textarea: {
    padding: '8px 12px',
    border: '1px solid #ced4da',
    borderRadius: '4px',
    fontSize: '1rem',
    minHeight: '80px',
    resize: 'vertical',
    ':focus': {
      outline: 'none',
      borderColor: '#80bdff',
      boxShadow: '0 0 0 3px rgba(0,123,255,0.25)',
    },
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
