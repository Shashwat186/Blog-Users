import { Link, Route, Routes, useNavigate, useParams } from 'react-router-dom'
import { useEffect, useState } from 'react'

const formatDate = (value) => value ? new Intl.DateTimeFormat(undefined, { dateStyle: 'medium' }).format(new Date(value)) : 'Unknown date'
const excerpt = (body = '') => body.length > 180 ? `${body.slice(0, 180)}…` : body

function Layout({ children }) {
  return <><header><Link className="brand" to="/">Shashwat</Link><span>My journey as a engineer</span></header><main>{children}</main><footer>© {new Date().getFullYear()} Inkstone</footer></>
}

function Home() {
  const [data, setData] = useState(null)
  const [error, setError] = useState('')
  const [page, setPage] = useState(0)

  useEffect(() => {
    setData(null); setError('')
    fetch(`/api/posts?page=${page}`).then(response => response.ok ? response.json() : Promise.reject())
      .then(setData).catch(() => setError('Posts could not be loaded. Is the Spring API running?'))
  }, [page])

  return <Layout><section className="hero"><p className="eyebrow">The blog</p><h1>Latest stories</h1><p>A quiet collection of ideas, notes, and things I've learned during as a software engineer.</p></section>
    {error && <p className="status error">{error}</p>}
    {!data && !error && <p className="status">Loading stories…</p>}
    {data?.content.length === 0 && <p className="status">No posts yet.</p>}
    <section className="grid">{data?.content.map(post => <article className="card" key={post.id}><p className="meta">{formatDate(post.createDate)} · {post.author}</p><h2><Link to={`/post/${post.id}`}>{post.title}</Link></h2><p>{excerpt(post.body)}</p><Link className="read" to={`/post/${post.id}`}>Read story <span>→</span></Link></article>)}</section>
    {data && <nav className="pagination"><button disabled={!data.hasPrevious} onClick={() => setPage(page - 1)}>← Newer</button><span>Page {data.page + 1} of {Math.max(1, data.totalPages)}</span><button disabled={!data.hasNext} onClick={() => setPage(page + 1)}>Older →</button></nav>}
  </Layout>
}

function Post() {
  const { id } = useParams(); const navigate = useNavigate(); const [post, setPost] = useState(null); const [error, setError] = useState('')
  useEffect(() => { fetch(`/api/posts/${id}`).then(r => r.ok ? r.json() : Promise.reject(r.status)).then(setPost).catch(() => setError('This story no longer exists.')) }, [id])
  return <Layout><button className="back" onClick={() => navigate(-1)}>← All stories</button>{error && <p className="status error">{error}</p>}{!post && !error && <p className="status">Loading story…</p>}{post && <article className="story"><p className="meta">{formatDate(post.createDate)} · {post.author}</p><h1>{post.title}</h1><p className="body">{post.body}</p>{post.comments?.length > 0 && <section className="comments"><h2>Comments</h2>{post.comments.map(comment => <article key={comment.id}><strong>{comment.author}</strong><span>{formatDate(comment.createDate)}</span><p>{comment.body}</p></article>)}</section>}</article>}</Layout>
}

export default function App() { return <Routes><Route path="/" element={<Home />} /><Route path="/post/:id" element={<Post />} /><Route path="*" element={<Home />} /></Routes> }
