interface TableProps<T> {
  data: T[];
  columns: { key: string; label: string; render?: (item: T) => React.ReactNode }[];
  onEdit?: (item: T) => void;
  onDelete?: (item: T) => void;
}

export function Table<T>({ data, columns, onEdit, onDelete }: TableProps<T>) {
  return (
    <div style={styles.container}>
      <table style={styles.table}>
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col.key} style={styles.th}>{col.label}</th>
            ))}
            {onEdit || onDelete && <th style={styles.th}>操作</th>}
          </tr>
        </thead>
        <tbody>
          {data.length === 0 ? (
            <tr>
              <td colSpan={columns.length + (onEdit || onDelete ? 1 : 0)} style={styles.empty}>
                暂无数据
              </td>
            </tr>
          ) : (
            data.map((item, index) => (
              <tr key={index} style={styles.tr}>
                {columns.map((col) => (
                  <td key={col.key} style={styles.td}>
                    {col.render ? col.render(item) : (item as Record<string, unknown>)[col.key]}
                  </td>
                ))}
                {(onEdit || onDelete) && (
                  <td style={styles.td}>
                    <div style={styles.actions}>
                      {onEdit && (
                        <button onClick={() => onEdit(item)} style={styles.btnEdit}>
                          编辑
                        </button>
                      )}
                      {onDelete && (
                        <button onClick={() => onDelete(item)} style={styles.btnDelete}>
                          删除
                        </button>
                      )}
                    </div>
                  </td>
                )}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

const styles = {
  container: {
    overflowX: 'auto',
    marginBottom: '1rem',
  },
  table: {
    width: '100%',
    borderCollapse: 'collapse',
    backgroundColor: '#fff',
    borderRadius: '8px',
    boxShadow: '0 2px 4px rgba(0,0,0,0.05)',
  },
  th: {
    textAlign: 'left',
    padding: '12px 16px',
    backgroundColor: '#f8f9fa',
    borderBottom: '2px solid #dee2e6',
    fontWeight: 600,
    color: '#495057',
  },
  tr: {
    borderBottom: '1px solid #dee2e6',
    ':hover': {
      backgroundColor: '#f8f9fa',
    },
  },
  td: {
    padding: '12px 16px',
    color: '#212529',
  },
  empty: {
    textAlign: 'center',
    padding: '2rem',
    color: '#6c757d',
  },
  actions: {
    display: 'flex',
    gap: '8px',
  },
  btnEdit: {
    padding: '4px 12px',
    backgroundColor: '#007bff',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontSize: '0.875rem',
    ':hover': {
      backgroundColor: '#0069d9',
    },
  },
  btnDelete: {
    padding: '4px 12px',
    backgroundColor: '#dc3545',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontSize: '0.875rem',
    ':hover': {
      backgroundColor: '#c82333',
    },
  },
};
