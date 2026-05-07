import { useState, useEffect } from 'react';
import { EntityType, CreateEntityTypeRequest } from '../types';
import { entityTypeApi } from '../api/eav';
import { Table } from '../components/Table';
import { Modal } from '../components/Modal';

export function EntityTypePage() {
  const [entityTypes, setEntityTypes] = useState<EntityType[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<EntityType | null>(null);
  const [formData, setFormData] = useState<CreateEntityTypeRequest>({
    code: '',
    name: '',
    description: '',
  });

  useEffect(() => {
    loadEntityTypes();
  }, []);

  const loadEntityTypes = async () => {
    try {
      const data = await entityTypeApi.getAll();
      setEntityTypes(data);
    } catch (error) {
      console.error('Failed to load entity types:', error);
    }
  };

  const handleOpenModal = (item?: EntityType) => {
    if (item) {
      setEditingItem(item);
      setFormData({
        code: item.code,
        name: item.name,
        description: item.description || '',
      });
    } else {
      setEditingItem(null);
      setFormData({ code: '', name: '', description: '' });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingItem(null);
    setFormData({ code: '', name: '', description: '' });
  };

  const handleSubmit = async () => {
    try {
      if (editingItem) {
        await entityTypeApi.update(editingItem.id, formData);
      } else {
        await entityTypeApi.create(formData);
      }
      handleCloseModal();
      loadEntityTypes();
    } catch (error) {
      console.error('Failed to save entity type:', error);
    }
  };

  const handleDelete = async (item: EntityType) => {
    if (confirm(`确定删除实体类型 "${item.name}" 吗？`)) {
      try {
        await entityTypeApi.delete(item.id);
        loadEntityTypes();
      } catch (error) {
        console.error('Failed to delete entity type:', error);
      }
    }
  };

  const columns = [
    { key: 'code', label: '编码' },
    { key: 'name', label: '名称' },
    { key: 'description', label: '描述' },
    { key: 'createdAt', label: '创建时间', render: (item) => new Date(item.createdAt).toLocaleString() },
  ];

  return (
    <div>
      <div style={styles.header}>
        <h2>实体类型管理</h2>
        <button onClick={() => handleOpenModal()} style={styles.btnAdd}>
          + 新增
        </button>
      </div>
      <Table
        data={entityTypes}
        columns={columns}
        onEdit={handleOpenModal}
        onDelete={handleDelete}
      />

      <Modal isOpen={isModalOpen} onClose={handleCloseModal} title={editingItem ? '编辑实体类型' : '新增实体类型'}>
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
            <label style={styles.label}>描述</label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              style={styles.textarea}
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
