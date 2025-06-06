PGDMP  2    
                }            vc-collections    17.2    17.2 *    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    55778    vc-collections    DATABASE     �   CREATE DATABASE "vc-collections" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Kazakh_Kazakhstan.utf8';
     DROP DATABASE "vc-collections";
                     postgres    false            �            1255    55849    increment_view_count(integer)    FUNCTION     �   CREATE FUNCTION public.increment_view_count(collection_id_param integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE collections
    SET view_count = view_count + 1
    WHERE collection_id = collection_id_param;
END;
$$;
 H   DROP FUNCTION public.increment_view_count(collection_id_param integer);
       public               postgres    false            �            1255    55852    limit_collaborators_count()    FUNCTION     z  CREATE FUNCTION public.limit_collaborators_count() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    collaborators_count INTEGER;
BEGIN
    -- Считаем текущее количество соавторов
    SELECT COUNT(*) INTO collaborators_count
    FROM collection_collaborators
    WHERE collection_id = NEW.collection_id;

    -- Если уже 10 или больше соавторов, то отклоняем добавление еще одного
    IF collaborators_count >= 10 THEN
        RAISE EXCEPTION 'Cannot add more than 10 collaborators to a collection';
    END IF;
    RETURN NEW;
END;
$$;
 2   DROP FUNCTION public.limit_collaborators_count();
       public               postgres    false            �            1255    55850    prevent_owner_as_collaborator()    FUNCTION     �  CREATE FUNCTION public.prevent_owner_as_collaborator() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    owner_id INTEGER;
BEGIN
    -- Получаем ID владельца коллекции (из этой же таблицы collections)
    SELECT user_id INTO owner_id
    FROM collections
    WHERE collection_id = NEW.collection_id;

    -- Если пытаемся добавить владельца как соавтора
    IF NEW.user_id IS NOT NULL AND NEW.user_id = owner_id THEN -- Добавлена проверка NEW.user_id IS NOT NULL
        RAISE EXCEPTION 'Collection owner cannot be added as a collaborator';
    END IF;
    RETURN NEW;
END;
$$;
 6   DROP FUNCTION public.prevent_owner_as_collaborator();
       public               postgres    false            �            1255    55847    update_collection_timestamp()    FUNCTION     �   CREATE FUNCTION public.update_collection_timestamp() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;
 4   DROP FUNCTION public.update_collection_timestamp();
       public               postgres    false            �            1259    55793    collection_collaborators    TABLE       CREATE TABLE public.collection_collaborators (
    collection_id integer NOT NULL,
    user_id integer NOT NULL,
    role character varying(20) DEFAULT 'editor'::character varying NOT NULL,
    joined_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);
 ,   DROP TABLE public.collection_collaborators;
       public         heap r       postgres    false            �            1259    55832    collection_comments    TABLE     �   CREATE TABLE public.collection_comments (
    comment_id integer NOT NULL,
    collection_id integer,
    user_id integer,
    comment_text text NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);
 '   DROP TABLE public.collection_comments;
       public         heap r       postgres    false            �            1259    55831 "   collection_comments_comment_id_seq    SEQUENCE     �   CREATE SEQUENCE public.collection_comments_comment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 9   DROP SEQUENCE public.collection_comments_comment_id_seq;
       public               postgres    false    223            �           0    0 "   collection_comments_comment_id_seq    SEQUENCE OWNED BY     i   ALTER SEQUENCE public.collection_comments_comment_id_seq OWNED BY public.collection_comments.comment_id;
          public               postgres    false    222            �            1259    55805    collection_items    TABLE     �   CREATE TABLE public.collection_items (
    collection_id integer NOT NULL,
    item_id integer NOT NULL,
    added_by_user_id integer,
    added_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    "position" integer,
    notes text
);
 $   DROP TABLE public.collection_items;
       public         heap r       postgres    false            �            1259    55820    collection_likes    TABLE     �   CREATE TABLE public.collection_likes (
    user_id integer NOT NULL,
    collection_id integer NOT NULL,
    liked_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);
 $   DROP TABLE public.collection_likes;
       public         heap r       postgres    false            �            1259    55780    collections    TABLE     �  CREATE TABLE public.collections (
    collection_id integer NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    cover_image_url character varying(255),
    user_id integer,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    is_public boolean DEFAULT false,
    view_count integer DEFAULT 0
);
    DROP TABLE public.collections;
       public         heap r       postgres    false            �            1259    55779    collections_collection_id_seq    SEQUENCE     �   CREATE SEQUENCE public.collections_collection_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.collections_collection_id_seq;
       public               postgres    false    218            �           0    0    collections_collection_id_seq    SEQUENCE OWNED BY     _   ALTER SEQUENCE public.collections_collection_id_seq OWNED BY public.collections.collection_id;
          public               postgres    false    217            ?           2604    55835    collection_comments comment_id    DEFAULT     �   ALTER TABLE ONLY public.collection_comments ALTER COLUMN comment_id SET DEFAULT nextval('public.collection_comments_comment_id_seq'::regclass);
 M   ALTER TABLE public.collection_comments ALTER COLUMN comment_id DROP DEFAULT;
       public               postgres    false    222    223    223            6           2604    55783    collections collection_id    DEFAULT     �   ALTER TABLE ONLY public.collections ALTER COLUMN collection_id SET DEFAULT nextval('public.collections_collection_id_seq'::regclass);
 H   ALTER TABLE public.collections ALTER COLUMN collection_id DROP DEFAULT;
       public               postgres    false    217    218    218            �          0    55793    collection_collaborators 
   TABLE DATA           [   COPY public.collection_collaborators (collection_id, user_id, role, joined_at) FROM stdin;
    public               postgres    false    219   �>       �          0    55832    collection_comments 
   TABLE DATA           k   COPY public.collection_comments (comment_id, collection_id, user_id, comment_text, created_at) FROM stdin;
    public               postgres    false    223    ?       �          0    55805    collection_items 
   TABLE DATA           q   COPY public.collection_items (collection_id, item_id, added_by_user_id, added_at, "position", notes) FROM stdin;
    public               postgres    false    220   r?       �          0    55820    collection_likes 
   TABLE DATA           L   COPY public.collection_likes (user_id, collection_id, liked_at) FROM stdin;
    public               postgres    false    221   @       �          0    55780    collections 
   TABLE DATA           �   COPY public.collections (collection_id, title, description, cover_image_url, user_id, created_at, updated_at, is_public, view_count) FROM stdin;
    public               postgres    false    218   �@       �           0    0 "   collection_comments_comment_id_seq    SEQUENCE SET     P   SELECT pg_catalog.setval('public.collection_comments_comment_id_seq', 1, true);
          public               postgres    false    222            �           0    0    collections_collection_id_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public.collections_collection_id_seq', 13, true);
          public               postgres    false    217            E           2606    55799 6   collection_collaborators collection_collaborators_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.collection_collaborators
    ADD CONSTRAINT collection_collaborators_pkey PRIMARY KEY (collection_id, user_id);
 `   ALTER TABLE ONLY public.collection_collaborators DROP CONSTRAINT collection_collaborators_pkey;
       public                 postgres    false    219    219            M           2606    55840 ,   collection_comments collection_comments_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.collection_comments
    ADD CONSTRAINT collection_comments_pkey PRIMARY KEY (comment_id);
 V   ALTER TABLE ONLY public.collection_comments DROP CONSTRAINT collection_comments_pkey;
       public                 postgres    false    223            G           2606    55812 &   collection_items collection_items_pkey 
   CONSTRAINT     x   ALTER TABLE ONLY public.collection_items
    ADD CONSTRAINT collection_items_pkey PRIMARY KEY (collection_id, item_id);
 P   ALTER TABLE ONLY public.collection_items DROP CONSTRAINT collection_items_pkey;
       public                 postgres    false    220    220            K           2606    55825 &   collection_likes collection_likes_pkey 
   CONSTRAINT     x   ALTER TABLE ONLY public.collection_likes
    ADD CONSTRAINT collection_likes_pkey PRIMARY KEY (user_id, collection_id);
 P   ALTER TABLE ONLY public.collection_likes DROP CONSTRAINT collection_likes_pkey;
       public                 postgres    false    221    221            B           2606    55791    collections collections_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.collections
    ADD CONSTRAINT collections_pkey PRIMARY KEY (collection_id);
 F   ALTER TABLE ONLY public.collections DROP CONSTRAINT collections_pkey;
       public                 postgres    false    218            N           1259    55846 %   idx_collection_comments_collection_id    INDEX     n   CREATE INDEX idx_collection_comments_collection_id ON public.collection_comments USING btree (collection_id);
 9   DROP INDEX public.idx_collection_comments_collection_id;
       public                 postgres    false    223            H           1259    55818 "   idx_collection_items_collection_id    INDEX     h   CREATE INDEX idx_collection_items_collection_id ON public.collection_items USING btree (collection_id);
 6   DROP INDEX public.idx_collection_items_collection_id;
       public                 postgres    false    220            I           1259    55819    idx_collection_items_item_id    INDEX     \   CREATE INDEX idx_collection_items_item_id ON public.collection_items USING btree (item_id);
 0   DROP INDEX public.idx_collection_items_item_id;
       public                 postgres    false    220            C           1259    55792    idx_collections_user_id    INDEX     R   CREATE INDEX idx_collections_user_id ON public.collections USING btree (user_id);
 +   DROP INDEX public.idx_collections_user_id;
       public                 postgres    false    218            T           2620    55853 2   collection_collaborators limit_collaborators_count    TRIGGER     �   CREATE TRIGGER limit_collaborators_count BEFORE INSERT ON public.collection_collaborators FOR EACH ROW EXECUTE FUNCTION public.limit_collaborators_count();
 K   DROP TRIGGER limit_collaborators_count ON public.collection_collaborators;
       public               postgres    false    227    219            U           2620    55851 6   collection_collaborators prevent_owner_as_collaborator    TRIGGER     �   CREATE TRIGGER prevent_owner_as_collaborator BEFORE INSERT ON public.collection_collaborators FOR EACH ROW EXECUTE FUNCTION public.prevent_owner_as_collaborator();
 O   DROP TRIGGER prevent_owner_as_collaborator ON public.collection_collaborators;
       public               postgres    false    226    219            S           2620    55848 '   collections update_collection_timestamp    TRIGGER     �   CREATE TRIGGER update_collection_timestamp BEFORE UPDATE ON public.collections FOR EACH ROW EXECUTE FUNCTION public.update_collection_timestamp();
 @   DROP TRIGGER update_collection_timestamp ON public.collections;
       public               postgres    false    218    224            O           2606    55800 D   collection_collaborators collection_collaborators_collection_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.collection_collaborators
    ADD CONSTRAINT collection_collaborators_collection_id_fkey FOREIGN KEY (collection_id) REFERENCES public.collections(collection_id) ON DELETE CASCADE;
 n   ALTER TABLE ONLY public.collection_collaborators DROP CONSTRAINT collection_collaborators_collection_id_fkey;
       public               postgres    false    219    4674    218            R           2606    55841 :   collection_comments collection_comments_collection_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.collection_comments
    ADD CONSTRAINT collection_comments_collection_id_fkey FOREIGN KEY (collection_id) REFERENCES public.collections(collection_id) ON DELETE CASCADE;
 d   ALTER TABLE ONLY public.collection_comments DROP CONSTRAINT collection_comments_collection_id_fkey;
       public               postgres    false    4674    223    218            P           2606    55813 4   collection_items collection_items_collection_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.collection_items
    ADD CONSTRAINT collection_items_collection_id_fkey FOREIGN KEY (collection_id) REFERENCES public.collections(collection_id) ON DELETE CASCADE;
 ^   ALTER TABLE ONLY public.collection_items DROP CONSTRAINT collection_items_collection_id_fkey;
       public               postgres    false    220    218    4674            Q           2606    55826 4   collection_likes collection_likes_collection_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.collection_likes
    ADD CONSTRAINT collection_likes_collection_id_fkey FOREIGN KEY (collection_id) REFERENCES public.collections(collection_id) ON DELETE CASCADE;
 ^   ALTER TABLE ONLY public.collection_likes DROP CONSTRAINT collection_likes_collection_id_fkey;
       public               postgres    false    4674    218    221            �   3   x���4�LM�,�/�4202�50�5�T02�22�21�377�60����� ���      �   b   x��1� �N�M�1�P�ѱK�P)`	Xz���y�F�����on}`��J��Zgtšu�$�[;�%9�Ĳ�,���D/.P��� �og��3Q/      �      x�e���0D�3U��	.���
�� ���<|L ����Avpv�b-!�/�=��1����p'*53��#����IgI"kL�����G\T`���R�4��InQ�����]4*	4�Nyх��7�*/B      �   �   x�m�Q
�0����5�l�Iβ��c�V�:
�| )���#vb#&u:���D͢?B7`�B&l�&����*��l'�Y=�E3�A�\�Zb+C\�?��C��	�fN������Y�q�QԾ��z����y���Z_�2�      �   �  x���Ao� ���WLN�ժ������c����f�6QLR���ݤ�[%��<k�{���{7E8�ƅ�n������a���'G���6nE8
�[�	2��L��5 7\ɩfj-�/&��҈�V���F�
<��:�0��>�x�|��������|E��Z"��c~bz�}�v0�z�"ԡ��<�<�MhN0u��7Iw������?��͝�I��+Ô����R��mꤿ?J#K�J���k�k5���S ��-9U���W)�9'O)B�\L;�|�3`������e�R(]�S�o���f7����Ώ��ϼ�`�(c�b{[V3��ݘ����|����Xw~la��.�^�]�9�cf����cui���v"����ak�a�;�L�CNA�̔ڠ�Z�K~A���=�iQo(�     