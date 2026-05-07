interface LayoutProps {
  children: React.ReactNode;
}

export function Layout({ children }: LayoutProps) {
  return (
    <div style={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
      <header style={styles.header}>
        <h1 style={styles.title}>MXT EAV 管理系统</h1>
      </header>
      <main style={styles.main}>{children}</main>
    </div>
  );
}

const styles = {
  header: {
    backgroundColor: '#2c3e50',
    color: '#fff',
    padding: '1rem 2rem',
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
  },
  title: {
    margin: 0,
    fontSize: '1.5rem',
    fontWeight: 600,
  },
  main: {
    padding: '2rem',
    maxWidth: '1200px',
    margin: '0 auto',
  },
};
